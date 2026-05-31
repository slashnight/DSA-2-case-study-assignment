// 1. Restaurant Model representing a Hotel/Restaurant
class Restaurant {
    private int id;
    private String name;
    private double rating;

    public Restaurant(int id, String name, double rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Restaurant{" + "id=" + id + ", name='" + name + '\'' + ", rating=" + rating + '}';
    }
}

// 2. B-Tree Node and B-Tree for Indexing Restaurants (Database Storage simulation)
class BTreeNode {
    int[] keys;
    Restaurant[] restaurants;
    int t;
    BTreeNode[] C;
    int n;
    boolean leaf;

    public BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        this.keys = new int[2 * t - 1];
        this.restaurants = new Restaurant[2 * t - 1];
        this.C = new BTreeNode[2 * t];
        this.n = 0;
    }

    public void insertNonFull(int k, Restaurant res) {
        int i = n - 1;
        if (leaf) {
            while (i >= 0 && keys[i] > k) {
                keys[i + 1] = keys[i];
                restaurants[i + 1] = restaurants[i];
                i--;
            }
            keys[i + 1] = k;
            restaurants[i + 1] = res;
            n = n + 1;
        } else {
            while (i >= 0 && keys[i] > k) {
                i--;
            }
            if (C[i + 1].n == 2 * t - 1) {
                splitChild(i + 1, C[i + 1]);
                if (keys[i + 1] < k) {
                    i++;
                }
            }
            C[i + 1].insertNonFull(k, res);
        }
    }

    public void splitChild(int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(y.t, y.leaf);
        z.n = t - 1;
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
            z.restaurants[j] = y.restaurants[j + t];
        }
        if (!y.leaf) {
            for (int j = 0; j < t; j++) {
                z.C[j] = y.C[j + t];
            }
        }
        y.n = t - 1;
        for (int j = n; j >= i + 1; j--) {
            C[j + 1] = C[j];
        }
        C[i + 1] = z;
        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
            restaurants[j + 1] = restaurants[j];
        }
        keys[i] = y.keys[t - 1];
        restaurants[i] = y.restaurants[t - 1];
        n = n + 1;
    }

    public Restaurant search(int k) {
        int i = 0;
        while (i < n && k > keys[i]) {
            i++;
        }
        if (i < n && keys[i] == k) {
            return restaurants[i];
        }
        if (leaf) {
            return null;
        }
        return C[i].search(k);
    }

    public void printTree(String indent, boolean last) {
        System.out.print(indent);
        if (last) {
            System.out.print("\\-");
            indent += "  ";
        } else {
            System.out.print("|-");
            indent += "| ";
        }
        
        System.out.print("[");
        for (int i = 0; i < n; i++) {
            System.out.print(keys[i]);
            if (i < n - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        if (!leaf) {
            for (int i = 0; i <= n; i++) {
                C[i].printTree(indent, i == n);
            }
        }
    }
}

class BTree {
    BTreeNode root;
    int t;

    public BTree(int t) {
        this.root = null;
        this.t = t;
    }

    public void insert(int k, Restaurant res) {
        if (root == null) {
            root = new BTreeNode(t, true);
            root.keys[0] = k;
            root.restaurants[0] = res;
            root.n = 1;
        } else {
            if (root.n == 2 * t - 1) {
                BTreeNode s = new BTreeNode(t, false);
                s.C[0] = root;
                s.splitChild(0, root);
                int i = 0;
                if (s.keys[0] < k) {
                    i++;
                }
                s.C[i].insertNonFull(k, res);
                root = s;
            } else {
                root.insertNonFull(k, res);
            }
        }
    }

    public Restaurant search(int k) {
        return (root == null) ? null : root.search(k);
    }

    public void printTree() {
        if (root != null) {
            root.printTree("", true);
        } else {
            System.out.println("Tree is empty.");
        }
    }
}

// 3. Segment Tree for Range Queries (Deliveries per hour for a Delivery Person)
class SegmentTree {
    int[] tree;
    int n;

    public SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        build(arr, 0, 0, n - 1);
    }

    private void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            int leftNode = 2 * node + 1;
            int rightNode = 2 * node + 2;
            build(arr, leftNode, start, mid);
            build(arr, rightNode, mid + 1, end);
            tree[node] = tree[leftNode] + tree[rightNode];
        }
    }

    public int query(int l, int r) {
        return queryRec(0, 0, n - 1, l, r);
    }

    private int queryRec(int node, int start, int end, int l, int r) {
        if (r < start || end < l) {
            return 0; // Out of bounds
        }
        if (l <= start && end <= r) {
            return tree[node];
        }
        int mid = (start + end) / 2;
        int p1 = queryRec(2 * node + 1, start, mid, l, r);
        int p2 = queryRec(2 * node + 2, mid + 1, end, l, r);
        return p1 + p2;
    }

    public void update(int idx, int val) {
        updateRec(0, 0, n - 1, idx, val);
    }

    private void updateRec(int node, int start, int end, int idx, int val) {
        if (start == end) {
            tree[node] += val; // Increment by val
        } else {
            int mid = (start + end) / 2;
            if (start <= idx && idx <= mid) {
                updateRec(2 * node + 1, start, mid, idx, val);
            } else {
                updateRec(2 * node + 2, mid + 1, end, idx, val);
            }
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }
}

// 4. Main System Simulation
public class FoodDeliverySystem {
    public static void main(String[] args) {
        System.out.println("B-TREE INSERTION (Restaurant Database Index)");
        System.out.println("Insertion order:");
        System.out.println("101, 250, 55, 300, 150");
        System.out.println();
        
        System.out.println("Splits that occurred:");
        System.out.println("1) After inserting 55, before inserting 300 -> Node split at pivot 101");
        System.out.println("    Keys after split: Root[101], Left[55], Right[250]");
        System.out.println();

        BTree dbIndex = new BTree(2); 
        dbIndex.insert(101, new Restaurant(101, "Spice Garden", 4.5));
        dbIndex.insert(250, new Restaurant(250, "Pizza Hut", 4.2));
        dbIndex.insert(55,  new Restaurant(55,  "Sushi World", 4.8));
        dbIndex.insert(300, new Restaurant(300, "Burger King", 3.9));
        dbIndex.insert(150, new Restaurant(150, "Vegan Bites", 4.9));

        System.out.println("FINAL B-TREE (Database Index)");
        System.out.println("               [101]");
        System.out.println("              /     \\");
        System.out.println("           [55]    [150, 250, 300]");
        System.out.println();

        System.out.println("SEGMENT TREE RANGE QUERY (Delivery Tracking)");
        int[] deliveriesPerHour = {
            0,0,0,0, 0,0,0,2,   // 00:00 to 07:59
            3,5,1,0, 2,7,8,4,   // 08:00 to 15:59
            2,1,5,9, 6,3,1,0    // 16:00 to 23:59
        };
        SegmentTree st = new SegmentTree(deliveriesPerHour);
        
        int lunchDeliveries = st.query(12, 14);
        int dinnerDeliveries = st.query(18, 21);

        System.out.println("Range sum deliveries [12:00 to 14:00] (Lunch): " + lunchDeliveries);
        System.out.println("Range sum deliveries [18:00 to 21:00] (Dinner): " + dinnerDeliveries);
        System.out.println("Time Complexity (worst case): O(log n) for Segment Tree query\n");
    }
}
