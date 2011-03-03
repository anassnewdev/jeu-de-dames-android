package android.dames;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PlateauView extends View {

	/* Parametres des cases */
	private static int mTailleCase;
	private static int mNbCases = 10;
	private static int mMarge = 0;
	/* Offset du d�but de damier */
	private static int mXOffset;
	private static int mYOffset;
	/* Tableau de cases */
	private int[][] mTableauCases = new int[10][10]; 
	/* Tableau contenant les cases */
    private Bitmap[] mCasesTab; 

    private final Paint mPaint = new Paint();

	public PlateauView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlateauView);
		mTailleCase = a.getInt(R.styleable.PlateauView_tailleCase, 20);
		a.recycle();
		
	}

	public PlateauView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlateauView);
		mTailleCase = a.getInt(R.styleable.PlateauView_tailleCase, 20);
		a.recycle();
	}

	/**
	 * Methode de (re)initialisation des cases du damier
	 * @param nbTypeCases
	 */
    public void resetCases(int nbTypeCases) {
    	mCasesTab = new Bitmap[nbTypeCases];
    }
	
	/**
	 * Methode chargeant les diff�rentes cases
	 * @param typeCase
	 * @param sprite
	 */
	public void loadCases(int typeCase, Drawable sprite) {
        Bitmap bitmap = Bitmap.createBitmap(mTailleCase, mTailleCase, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        sprite.setBounds(0, 0, mTailleCase, mTailleCase);
        sprite.draw(canvas);
        mCasesTab[typeCase] = bitmap;
	}
    
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
        for (int x = 0; x < mNbCases; x += 1) {
            for (int y = 0; y < mNbCases; y += 1) {
                if (mTableauCases[x][y] > 0) {
                    canvas.drawBitmap(mCasesTab[mTableauCases[x][y]], 
                    		mXOffset + x * mTailleCase,
                    		mYOffset + y * mTailleCase,
                    		mPaint);
                }
            }
        }
	}
	
	/**
	 * Methode appelee lors de l'affichage. Permet d'obtenir la taille de l'ecran.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int cote = Math.min(w, h);
		mTailleCase = (int) Math.floor((cote-mMarge) / mNbCases);

		mXOffset = ((w - (mTailleCase * mNbCases)) / 2);
		mYOffset = ((h - (mTailleCase * mNbCases)) / 2);

        clearCases();
	}

	private void clearCases() {
        for (int x = 0; x < mNbCases; x++) {
            for (int y = 0; y < mNbCases; y++) {
                setTile(((x+y)%2)+1, x, y);
            }
        }
	}

	private void setTile(int type, int x, int y) {
        mTableauCases[x][y] = type;		
	}
}
