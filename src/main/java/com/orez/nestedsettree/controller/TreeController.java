package com.orez.nestedsettree.controller;

import com.orez.nestedsettree.model.NodeDTO;
import com.orez.nestedsettree.service.NodeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tree")
public class TreeController {

    private NodeService nodeService;

    public TreeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public NodeDTO getTree() {
        return nodeService.getTree();
    }
}
