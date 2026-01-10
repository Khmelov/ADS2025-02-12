package by.it.group451002.morozov.lesson14;

// Система непересекающихся множеств
class DSU {
	private int[] parent; // Родительские элементы
	private int[] rank;   // Ранги для балансировки
	private int[] size;   // Размеры множеств

	public DSU(int n) {
		parent = new int[n];
		rank = new int[n];
		size = new int[n];

		// Инициализация каждого элемента как корня
		for (int i = 0; i < n; i++) {
			parent[i] = i;
			size[i] = 1;
		}
	}

	// Эвристика сжатия пути
	public int find(int x) {
		if (parent[x] != x) {
			parent[x] = find(parent[x]);
		}
		return parent[x];
    }

    // Эвристика объединения по рангу
    public void union(int x, int y) {
    	int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return;

        if (rank[rootX] < rank[rootY]) {
        	parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
            rank[rootX]++;
        }
    }

    // Размер множества
    public int getSize(int x) {
    	return size[find(x)];
    }
}