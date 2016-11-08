package com.malviya.demoflootit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains whole game logic
 */
public class GameWorld extends View {
    private static final int COL = 6;
    private static final int ROW = 6;
    private static final int TAG_COL = 1;
    private static final int TAG_COLOR = 0;
    private static final int TAG_ROW = 2;
    private final int TOTAL_BUTTON = 4;
    private static final float INIT_TABLE_X = 0;
    private static final float INIT_TABLE_Y = 0;
    private static float CELL_W;
    private static float CELL_H;
    private Context mContext;
    private CellInfo[][] matrix;
    private int randomColor;
    private int widthSize;
    private int heightSize;

    public GameWorld(Context pContext) {
        super(pContext);
        mContext = pContext;
        matrix = new CellInfo[ROW][COL];
        CELL_W = Utils.convertPixelsToDp(150, mContext);
        CELL_H = Utils.convertPixelsToDp(150, mContext);
        initTable();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        canvas.drawRect(0, 0, widthSize, heightSize, paint);
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(Utils.convertPixelsToDp(0, mContext));
        paint.setStyle(Paint.Style.STROKE);
        drawRectTable(canvas, paint);
    }


   /* private void drawRectTable(Canvas canvas, Paint mGridPaint) {
        for (int col = 0; col < COL; col++) {
            for (int row = 0; row < ROW; row++) {
                canvas.drawRect(INIT_TABLE_X + (col * CELL_W), INIT_TABLE_Y + (CELL_H * row), (col + 1) * CELL_W, (row + 1) * CELL_H, mGridPaint);
            }
        }

    }*/

   /* private void drawRectTable(Canvas canvas, Paint mGridPaint) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(Utils.convertPixelsToDp(5, mContext));
        paint.setStyle(Paint.Style.FILL);
        for (CellInfo cell : mCellList) {
            paint.setColor(cell.getColor());
            canvas.drawRect(INIT_TABLE_X + (cell.getCol() * CELL_W), INIT_TABLE_Y + (CELL_H * cell.getRow()), (cell.getCol() + 1) * CELL_W, (cell.getRow() + 1) * CELL_H, paint);
        }


    }*/

    private void drawRectTable(Canvas canvas, Paint mGridPaint) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(Utils.convertPixelsToDp(0, mContext));
        paint.setStyle(Paint.Style.FILL);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                paint.setColor(matrix[row][col].getColor());
                canvas.drawRect(INIT_TABLE_X + (matrix[row][col].getCol() * CELL_W), INIT_TABLE_Y + (CELL_H * matrix[row][col].getRow()), (matrix[row][col].getCol() + 1) * CELL_W, (matrix[row][col].getRow() + 1) * CELL_H, paint);
            }
        }

    }


    /*private void initTable() {
        mCellList.clear();
        for (int col = 0; col < COL; col++) {
            for (int row = 0; row < ROW; row++) {
                mCellList.add(new CellInfo(getRandomColor(Math.random() * 10), col, row));
            }
        }
    }*/
    private void initTable() {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = new CellInfo(getRandomColor(Math.random() * 10), col, row);
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Get the width measurement
        widthSize = View.resolveSize((int) ((COL * CELL_W)), widthMeasureSpec);
        //Get the height measurement
        heightSize = View.resolveSize((int) ((ROW * CELL_H)), heightMeasureSpec);
        //MUST call this to store the measurements
        setMeasuredDimension(widthSize, heightSize);
    }


    public int getRandomColor(double index) {
        switch ((int) (index % TOTAL_BUTTON)) {
            case 0:
                return Color.RED;
            case 1:
                return Color.WHITE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLUE;
        }
        return Color.BLACK;
    }

    public void onReset() {
        initTable();
        invalidate();
    }


    public void findCellChainAndFloodWithColor(int selectedColor) {
        hashMap.clear();
        cellSearch(0, 0, selectedColor);
        floodWithColor();
        invalidate();
    }

    private void floodWithColor() {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                int key = ((row * ROW) + (col));
                if (hashMap.get(key) != null)
                    matrix[row][col].setColor(hashMap.get(key));
            }
        }
    }

    HashMap<Integer, Integer> hashMap = new HashMap<>();

    private void cellSearch(int row, int col, int selectedColor) {
        if (hashMap.get(0) == null) {
            hashMap.put(0, selectedColor);
        }
        row = row%ROW;
        col = col%COL;
        if ((col + 1) < COL) {
            if (matrix[row][col + 1].getColor() == matrix[row][col].getColor()) {
                int key = ((row * ROW) + (col + 1));
                if (hashMap.get(key) == null) {
                    hashMap.put(key, selectedColor);
                }
                cellSearch(row, (col + 1), selectedColor);
            }
        }




        if ((row + 1) < ROW) {
            if (matrix[row + 1][col].getColor() == matrix[row][col].getColor()) {
                int key = (((row + 1) * ROW) + (col));
                if (hashMap.get(key) == null) {
                    hashMap.put(key, selectedColor);
                }
                cellSearch((row + 1), col, selectedColor);
            }
        }
    }
}
