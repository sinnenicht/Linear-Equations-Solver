package solver;

import java.util.Arrays;

public class Variables {
    private int[] columnIndexes;

    public Variables(Matrix matrix) {
        int numVariables = matrix.getNumRows();
        int[] columnIndexes = new int[numVariables];
        for (int rowIndex = 0; rowIndex < numVariables; rowIndex++) {
            columnIndexes[rowIndex] = rowIndex;
        }
        System.out.println("Column Indexes: " + Arrays.toString(columnIndexes));
        this.columnIndexes = columnIndexes;
    }

    public int[] getColumnIndexes() {
        return columnIndexes;
    }

    public void switchColumnIndexes(int rowIndex1, int rowIndex2) {
        int var1PrevIndex = columnIndexes[rowIndex1];
        int var2PrevIndex = columnIndexes[rowIndex2];
        columnIndexes[rowIndex1] = var2PrevIndex;
        columnIndexes[rowIndex2] = var1PrevIndex;
        System.out.println("Column indexes: " + Arrays.toString(columnIndexes));
    }
}
