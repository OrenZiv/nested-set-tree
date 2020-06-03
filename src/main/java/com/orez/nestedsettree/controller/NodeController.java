package com.orez.nestedsettree.controller;

import com.orez.nestedsettree.exception.NodeNotFoundException;
import com.orez.nestedsettree.model.NodeDTO;
import com.orez.nestedsettree.service.NodeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/node")
public class NodeController {

    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public List<NodeDTO> getNode() {
        return nodeService.getSortedTreeNodes();
    }

    @GetMapping("/{uuid}")
    public NodeDTO getNode(@PathVariable UUID uuid) {
        return nodeService.getNode(uuid)
                .orElseThrow(() -> new NodeNotFoundException(String.format("Node with id %s was not found!", uuid)) );
    }

    @PostMapping
    public NodeDTO addNode(@RequestBody NodeDTO nodeDTO) {
        return nodeService.insertNode(nodeDTO.getParentId(), nodeDTO.getDescription());
    }

    @PatchMapping("/{uuid}")
    public HttpStatus moveNode(@PathVariable UUID uuid, @RequestBody NodeDTO nodeDTO) {
        nodeService.updateNode(getNode(uuid).getNodeId(), nodeDTO);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{uuid}")
    public HttpStatus deleteNode(@PathVariable UUID uuid) {
        nodeService.deleteNode(getNode(uuid).getNodeId());
        return HttpStatus.OK;
    }
}
