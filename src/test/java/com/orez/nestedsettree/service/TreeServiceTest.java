package com.orez.nestedsettree.service;

import com.orez.nestedsettree.exception.NodesCorruptedException;
import com.orez.nestedsettree.exception.NodesNotOrderedException;
import com.orez.nestedsettree.model.NodeDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TreeServiceTest {

    @Mock
    private NodeService nodeService;

    @InjectMocks
    private TreeService treeService;

    @Test(expected = NodesNotOrderedException.class)
    public void nodesNotSortedWhenBuildingTree() {
        when(nodeService.getSortedTreeNodes()).thenReturn(nodesList(1,4,2,3));
        treeService.getNestedSetTree();
    }

    @Test(expected = NodesCorruptedException.class)
    public void nodesLftsDuplicateWhenBuildingTree() {
        when(nodeService.getSortedTreeNodes()).thenReturn(nodesList(1,2,2,3,4));
        treeService.getNestedSetTree();
    }

    private List<NodeDTO> nodesList(Integer... lfts) {
        return Arrays.stream(lfts)
                .map(lft -> NodeDTO.builder().lft(lft).build())
                .collect(Collectors.toList());
    }
}
