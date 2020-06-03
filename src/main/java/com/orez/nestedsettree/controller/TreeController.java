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

    @GetMapping
    public NodeDTO getTree() {
        return treeService.getTree();
    }
}
