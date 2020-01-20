CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tree_map
(
  node_id UUID NOT NULL DEFAULT uuid_generate_v4(),
  lft INTEGER NOT NULL,
  rgt INTEGER NOT NULL,
  parent_id UUID,
  description CHARACTER VARYING(255),
  PRIMARY KEY (node_id)
);

CREATE OR REPLACE VIEW vw_lftrgt AS
	SELECT tree_map.lft AS lft
	FROM tree_map
	UNION
	SELECT tree_map.rgt AS rgt
	FROM tree_map;

INSERT INTO tree_map (node_id, lft, rgt, description)
VALUES (uuid_generate_v4(), 1, 2, 'Root');

CREATE OR REPLACE FUNCTION insert_node(p_parent_uuid UUID, p_description CHARACTER VARYING(255)) RETURNS UUID AS
$BODY$
DECLARE
	new_uuid UUID;
	new_lft INTEGER;
BEGIN
	SELECT uuid_generate_v4()
	INTO new_uuid;

	SELECT rgt
	INTO new_lft
	FROM tree_map
	WHERE node_id = p_parent_uuid;

	UPDATE tree_map
	SET rgt = (rgt + 2)
	WHERE rgt >= new_lft;

	UPDATE tree_map
	SET lft = (lft + 2)
	WHERE lft > new_lft;

	INSERT INTO tree_map (node_id, lft, rgt, parent_id, description)
	VALUES (new_uuid, new_lft, (new_lft + 1), p_parent_uuid, p_description);

	RETURN new_uuid;
END;
$BODY$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete_node(p_node_uuid UUID) RETURNS INTEGER AS
$BODY$
DECLARE
	superior_parent UUID;
	new_lft INTEGER;
	new_rgt INTEGER;
	has_leafs INTEGER;
	width INTEGER;
BEGIN
	SELECT lft, rgt, (rgt - lft), (rgt - lft + 1), parent_id
	INTO new_lft, new_rgt, has_leafs, width, superior_parent
	FROM tree_map
	WHERE node_id = p_node_uuid;

	IF (has_leafs = 1) THEN
	  DELETE FROM tree_map
	  WHERE lft BETWEEN new_lft AND new_rgt;

	  UPDATE tree_map
	  SET rgt = (rgt - width)
	  WHERE rgt > new_rgt;

	  UPDATE tree_map
	  SET lft = (lft - width)
	  WHERE lft > new_rgt;

	ELSE
	  DELETE FROM tree_map
	  WHERE lft = new_lft;

	  UPDATE tree_map
	  SET rgt = (rgt - 1), lft = (lft - 1), parent_id = superior_parent
	  WHERE lft BETWEEN new_lft AND new_rgt;

	  UPDATE tree_map
	  SET rgt = (rgt - 2)
	  WHERE rgt > new_rgt;

	  UPDATE tree_map
	  SET lft = (lft - 2)
	  WHERE lft > new_rgt;

	END IF;

	RETURN 1;
END;
$BODY$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION move_node(p_node_uuid UUID, p_to_parent_uuid UUID) RETURNS INTEGER AS
$BODY$
DECLARE
	subtree_size INTEGER;
	parent_rgt INTEGER;
BEGIN
	IF (p_node_uuid != p_to_parent_uuid) THEN
		CREATE TEMPORARY TABLE IF NOT EXISTS working_tree_map
		(
		  node_id UUID NOT NULL DEFAULT uuid_generate_v4(),
		  lft INTEGER DEFAULT NULL,
		  rgt INTEGER DEFAULT NULL,
		  parent_id UUID NOT NULL,
		  description TEXT,
		  PRIMARY KEY (node_id)
		);

		-- put subtree into temporary table
		INSERT INTO working_tree_map (node_id, lft, rgt, parent_id, description)
			 SELECT t1.node_id,
					(t1.lft - (SELECT MIN(lft)
								FROM tree_map
								WHERE node_id = p_node_uuid)) AS lft,
					(t1.rgt - (SELECT MIN(lft)
								FROM tree_map
								WHERE node_id = p_node_uuid)) AS rgt,
					t1.parent_id,
					t1.description
			   FROM tree_map AS t1, tree_map AS t2
			  WHERE t1.lft BETWEEN t2.lft AND t2.rgt
				AND t2.node_id = p_node_uuid;

		DELETE FROM tree_map
		WHERE node_id
		IN (SELECT node_id
			FROM working_tree_map);

		SELECT rgt
		INTO parent_rgt
		FROM tree_map
		WHERE node_id = p_to_parent_uuid;

		subtree_size = (SELECT (MAX(rgt) + 1)
						FROM working_tree_map);

		-- make a gap in the tree
		UPDATE tree_map
		  SET lft = (CASE
						WHEN lft > parent_rgt
						THEN lft + subtree_size
						ELSE lft
					 END),
			  rgt = (CASE
						WHEN rgt >= parent_rgt
						THEN rgt + subtree_size
						ELSE rgt
					 END)
		WHERE rgt >= parent_rgt;

		INSERT INTO tree_map (node_id, lft, rgt, parent_id, description)
		SELECT node_id, lft + parent_rgt, rgt + parent_rgt, parent_id, description
		FROM working_tree_map;

		-- close gaps in tree
		UPDATE tree_map
		SET lft = (SELECT COUNT(*)
					FROM vw_lftrgt AS v
					WHERE v.lft <= tree_map.lft),
			rgt = (SELECT COUNT(*)
					FROM vw_lftrgt AS v
					WHERE v.lft <= tree_map.rgt);

		DELETE FROM working_tree_map;

		UPDATE tree_map
		SET parent_id = p_to_parent_uuid
		WHERE node_id = p_node_uuid;
	END IF;

	RETURN 1;
END;
$BODY$
LANGUAGE plpgsql;