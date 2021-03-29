package app.holdingUnits.containers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test Node class.
 *
 * @version 1.0.0 17 Mar 2021
 * @author Aleksey Lakhanksii
 *
 */
class NodeTest {

    /**
     * deletes all nodes which could be created at other tests.
     */
    public NodeTest () {
        Node.cleanUp();
    }

    /**
     * Creates two nodes. Checks are this different nodes and are they both were saved in the nodes storage.
     */
    @Test
    public void builderTest() {
        String node1Name = "node1NodeTest1";
        String node2Name = "node2NodeTest1";
        Node node1 = Node.createNode(node1Name);
        Node node1Copy = Node.createNode(node1Name);
        Node node2 = Node.createNode(node2Name);

        assertNotNull(node1);
        assertEquals(node1Name, node1.getName());
        assertNull(node1Copy);
        assertNotNull(node2);
        assertNotEquals(node2, node1);
        assertTrue(Node.checkExistence(node1Name));
        assertTrue(Node.checkExistence(node2Name));
        assertEquals(node1, Node.nodesForNames.get(node1Name));
        assertEquals(node2, Node.nodesForNames.get(node2Name));
    }
}