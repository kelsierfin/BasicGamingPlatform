public class Board {
    int[][] matrix = new int[19][19];
    public void initialize(Board b){
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                matrix[i][j] = 0;
            }
        }

    }
}
