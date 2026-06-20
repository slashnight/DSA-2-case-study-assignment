import java.util.*;

class TreeNode {
    int key;
    TreeNode left, right;
    int height;
    int bf; // Balance Factor

    TreeNode(int key) {
        this.key = key;
        this.height = 1;
        this.bf = 0;
    }
}

public class FoodChainAVL {
    TreeNode root;
    List<String> rotations = new ArrayList<>();
    int rotationCount = 0;

    int getHeight(TreeNode node) {
        return node == null ? 0 : node.height;
    }

    void updateHeightAndBf(TreeNode node) {
        if (node == null) return;
        int leftH = getHeight(node.left);
        int rightH = getHeight(node.right);
        node.height = 1 + Math.max(leftH, rightH);
        node.bf = leftH - rightH;
    }

    TreeNode rightRotate(TreeNode z, int insertedKey) {
        TreeNode y = z.left;
        TreeNode T3 = y.right;
        
        y.right = z;
        z.left = T3;
        
        updateHeightAndBf(z);
        updateHeightAndBf(y);
        
        rotationCount++;
        rotations.add(rotationCount + ") After inserting " + insertedKey + " -> LL Rotation at pivot " + z.key + 
                "\n   Balances after rotation: " + z.key + "(bf=" + (z.bf > 0 ? "+" : "") + z.bf + "), " + 
                y.key + "(bf=" + (y.bf > 0 ? "+" : "") + y.bf + ")");
        return y;
    }

    TreeNode leftRotate(TreeNode z, int insertedKey) {
        TreeNode y = z.right;
        TreeNode T2 = y.left;
        
        y.left = z;
        z.right = T2;
        
        updateHeightAndBf(z);
        updateHeightAndBf(y);
        
        rotationCount++;
        rotations.add(rotationCount + ") After inserting " + insertedKey + " -> RR Rotation at pivot " + z.key + 
                "\n   Balances after rotation: " + z.key + "(bf=" + (z.bf > 0 ? "+" : "") + z.bf + "), " + 
                y.key + "(bf=" + (y.bf > 0 ? "+" : "") + y.bf + ")");
        return y;
    }

    TreeNode insert(TreeNode node, int key, int originalKey) {
        if (node == null) return new TreeNode(key);

        if (key < node.key) {
            node.left = insert(node.left, key, originalKey);
        } else if (key > node.key) {
            node.right = insert(node.right, key, originalKey);
        } else {
            return node;
        }

        updateHeightAndBf(node);
        int balance = node.bf;

        // LL Case
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node, originalKey);
        }
        // RR Case
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node, originalKey);
        }
        // LR Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left, originalKey);
            return rightRotate(node, originalKey);
        }
        // RL Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right, originalKey);
            return leftRotate(node, originalKey);
        }

        return node;
    }

    void insertKey(int key) {
        root = insert(root, key, key);
    }

    // -- Tree Printing Logic --
    static class NodePos {
        TreeNode node;
        int x;
        String str;
        NodePos(TreeNode n) { this.node = n; }
    }

    String buildTreeString() {
        if (root == null) return "";
        List<TreeNode> inorderSeq = new ArrayList<>();
        reverseInorder(root, inorderSeq);

        Map<TreeNode, NodePos> map = new HashMap<>();
        int colWidth = 15;
        for (int i = 0; i < inorderSeq.size(); i++) {
            TreeNode n = inorderSeq.get(i);
            NodePos np = new NodePos(n);
            np.str = n.key + "(bf=" + (n.bf > 0 ? "+" : "") + n.bf + ")";
            np.x = i * colWidth;
            map.put(n, np);
        }

        centerParents(root, map, colWidth);

        StringBuilder sb = new StringBuilder();
        List<TreeNode> queue = new ArrayList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            StringBuilder nodeLine = new StringBuilder();
            StringBuilder slashLine = new StringBuilder();
            List<TreeNode> nextQueue = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode n = queue.get(i);
                NodePos np = map.get(n);

                int spacesToNode = Math.max(0, np.x - nodeLine.length());
                for (int s = 0; s < spacesToNode; s++) nodeLine.append(" ");
                nodeLine.append(np.str);

                if (n.right != null) {
                    NodePos rightChild = map.get(n.right);
                    int slashPos = (rightChild.x + np.x + np.str.length() / 2) / 2;
                    int spaces = Math.max(0, slashPos - slashLine.length());
                    for (int s = 0; s < spaces; s++) slashLine.append(" ");
                    slashLine.append("/");
                    nextQueue.add(n.right);
                }
                if (n.left != null) {
                    NodePos leftChild = map.get(n.left);
                    int slashPos = (np.x + np.str.length() / 2 + leftChild.x) / 2;
                    int spaces = Math.max(0, slashPos - slashLine.length());
                    for (int s = 0; s < spaces; s++) slashLine.append(" ");
                    slashLine.append("\\");
                    nextQueue.add(n.left);
                }
            }
            sb.append(nodeLine.toString()).append("\n");
            if (!nextQueue.isEmpty()) {
                nextQueue.sort(Comparator.comparingInt(a -> map.get(a).x));
                sb.append(slashLine.toString()).append("\n");
            }
            queue = nextQueue;
        }
        return sb.toString();
    }

    void reverseInorder(TreeNode node, List<TreeNode> list) {
        if (node == null) return;
        reverseInorder(node.right, list);
        list.add(node);
        reverseInorder(node.left, list);
    }

    void centerParents(TreeNode node, Map<TreeNode, NodePos> map, int colWidth) {
        if (node == null) return;
        centerParents(node.right, map, colWidth);
        centerParents(node.left, map, colWidth);
        NodePos np = map.get(node);
        if (node.left != null && node.right != null) {
            np.x = (map.get(node.left).x + map.get(node.right).x) / 2;
        } else if (node.left != null) {
            np.x = map.get(node.left).x - (int)(colWidth / 1.5);
        } else if (node.right != null) {
            np.x = map.get(node.right).x + (int)(colWidth / 1.5);
        }
    }

    void getTopK(TreeNode node, int k, List<Integer> list) {
        if (node == null || list.size() >= k) return;
        getTopK(node.right, k, list);
        if (list.size() < k) list.add(node.key);
        getTopK(node.left, k, list);
    }

    public static void main(String[] args) {
        int[] keys = {45, 25, 60, 15, 35, 55, 75, 10, 20};
        FoodChainAVL tree = new FoodChainAVL();

        System.out.println("AVL INSERTION (Arrival Order)");
        System.out.println("Insertion order:");
        for (int i = 0; i < keys.length; i++) {
            System.out.print(keys[i] + (i < keys.length - 1 ? ", " : ""));
            tree.insertKey(keys[i]);
        }
        
        System.out.println("\nRotations that occurred:");
        if (tree.rotations.isEmpty()) {
            System.out.println("None (Tree remained balanced)");
        } else {
            for (String r : tree.rotations) {
                System.out.println(r);
            }
        }

        System.out.println("FINAL AVL TREE (Descending by timestamp)");
        System.out.println(tree.buildTreeString());

        System.out.println("TOP 5 DESCENDING (k = 5)");
        System.out.println("Top 5 timestamps (most recent first):");
        List<Integer> top5 = new ArrayList<>();
        tree.getTopK(tree.root, 5, top5);
        System.out.println(top5);
        System.out.println("Time Complexity (worst case): O(min(n, k) + log n)");
    }
}
