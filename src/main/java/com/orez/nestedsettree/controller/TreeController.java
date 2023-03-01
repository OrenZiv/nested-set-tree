package com.orez.nestedsettree.controller;

import com.orez.nestedsettree.model.NodeDTO;
import com.orez.nestedsettree.service.TreeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tree")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping("/nestedSet")
    public NodeDTO getNestedSetTree() {
        return treeService.getNestedSetTree();
    }

    @GetMapping("/adjacencyList")
    public NodeDTO getAdjacencyListTree() {
        return treeService.getAdjacencyListTree();
    }

    @GetMapping("/inMemAdjacencyList")
    public NodeDTO getInMemAdjacencyListTree() {
        return treeService.getAdjacencyListTreeInMem();
    }
}
