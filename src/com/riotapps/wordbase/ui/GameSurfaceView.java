package com.riotapps.wordbase.ui;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.wordbase.GameSurface;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.hooks.AlphabetService;
import com.riotapps.wordbase.hooks.PlayedTile;
import com.riotapps.wordbase.hooks.GameService;
import com.riotapps.wordbase.hooks.TileLayout;
import com.riotapps.wordbase.hooks.TileLayoutService;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Check;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;
import com.riotapps.wordbase.utils.ImageHelper;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.PreconditionException;
import com.riotapps.wordbase.utils.Utils;

import android.util.Log;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class GameSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

	GameSurface parent;
	private static final String TAG = GameSurfaceView.class.getSimpleName();
	
	public void setParent(GameSurface parent){
		this.parent = parent;
	}
	
//	ApplicationContext appContext;
	private boolean autoZoom = true;
	
	private CustomButtonDialog dialog = null;
	 private static Bitmap bgPlacedTileFull;
	private static Bitmap bgPlacedTileZoomed;
	private static Bitmap bgPlayedTileFull;
	private static Bitmap bgPlayedTileZoomed;
	private static Bitmap bgLastPlayedTileFull;
	private static Bitmap bgLastPlayedTileZoomed;
	
	private static Bitmap bgBaseScaled = null;
	private static Bitmap bgBaseZoomed = null;
	private static Bitmap bg4LScaled = null;
	private static Bitmap bg4LZoomed = null;
	private static Bitmap bg3LScaled = null;
	private static Bitmap bg3LZoomed = null;
	private static Bitmap bg3WScaled = null;
	private static Bitmap bg3WZoomed = null;
	private static Bitmap bg2LScaled = null;
	private static Bitmap bg2LZoomed = null; 
	private static Bitmap bg2WScaled = null;
	private static Bitmap bg2WZoomed = null;	
	private static Bitmap bgStarterScaled = null;
	private static Bitmap bgStarterZoomed = null;
	private static Bitmap bgTrayBaseScaled = null;
	private static Bitmap bgTrayEmptyScaled = null;
	private static Bitmap bgTrayBaseDragging = null;
 //	private static Bitmap bgTrayBackground = null;
	
	int absoluteTop = 0;
	int absoluteLeft = 0;
	
	GameSurfaceView me = this;
//	Context context;
	GameThread gameThread = null;
	boolean isThreadRunning = false;
	SurfaceHolder surfaceHolder;
	//Typeface bonusTypeface;
	//Typeface letterTypeface;
	//Typeface letterValueTypeface;
	private int currentX = 0;
    private int currentY = 20;
    private int fullWidth;
    private int fullViewTileWidth;
    private int trayHeight = 0;
    private boolean isTablet = false;
    private int onDrawCounter = 0;
    //private int trayAreaTop = 0;
    
    private boolean trayTileTapped = false;
    private boolean trayTileDropped = false;
    private boolean trayTileDropTarget = false;
    private boolean isButtonStateInShuffle = true;  //this is the default value because board starts in shuffle state
    private RectArea trayAreaRect = new RectArea();
    private RectArea visibleAreaRect = new RectArea();
    private RectArea boardAreaRect = new RectArea();
    private RectArea region1FullViewRect = new RectArea();
    private RectArea region2FullViewRect = new RectArea();
    private RectArea region3FullViewRect = new RectArea();
    private RectArea region4FullViewRect = new RectArea();
    private RectArea region5FullViewRect = new RectArea();
    private RectArea region6FullViewRect = new RectArea();
    private RectArea region7FullViewRect = new RectArea();
    private RectArea region8FullViewRect = new RectArea();
    private RectArea region9FullViewRect = new RectArea();

  //  private int top;
  //  private int left;
    private boolean isZoomed = false;
    private int excessWidth;
    private boolean isZoomAllowed = true; //if width of board greater than x disable zooming.  it means we are on a tablet and zooming not needed.
    private long tapCheck = 0;
    private float zoomMultiplier = 2.0f;
    private final int tileGap = 1;
    private static final int TRAY_TILE_GAP = 3;
    private int zoomedTileWidth;
    private int midpoint;
    private static final int TRAY_VERTICAL_MARGIN = 5;
    private static final int TRAY_TOP_BORDER_HEIGHT = 4;
//    private static final long SINGLE_TAP_DURATION_IN_NANOSECONDS = 550000000;
//    private static final long DOUBLE_TAP_DURATION_IN_NANOSECONDS = 800000000;
    private static final long SINGLE_TAP_DURATION_IN_MILLISECONDS = 550;
    private static final long DOUBLE_TAP_DURATION_IN_MILLISECONDS = 800;
    
    private static final long MOVE_STOPPED_DURATION_IN_MILLISECONDS = 200;
    
    private static final int MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD = 3;
    private float movementTriggerTapcheckThreshold = 3;
    private float movementTriggerTapcheckThresholdPercentageOfTotalWidth = .00625f;
//    private static final float MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD = .02f;
    private static final float MOVEMENT_TRIGGER_THRESHOLD = .05f;
  //  private static final int MAX_LOGO_HEIGHT = 60;
  //  private static final long MAX_TEXT_HEIGHT = 28;
    private static final float MAX_TEXT_WIDTH = .90F;
    private int xPosition = 0;
    private int yPosition = 0;
    private int xDistance = 0;
    private int yDistance = 0;
    
    private static final int NUMBER_OF_COORDINATES_TO_TRIGGER_MOMENTUM_SCROLLING = 3;
    private static final int NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED = 3;
    private long momentumScrollInterval = 20;
    
    private int bottomOfFullView;
    private int topGapHeight;
    private int bottomGapHeight;
    
    private int trayTileSize;
    private int draggingTileSize;
    private int trayTileLeftMargin;
    private int trayTop;
    //private int touchMotion = -1;
    private int outerZoomLeft;
    private int outerZoomTop; 
    private int fullViewTileMidpoint;
    private int zoomedTileMidpoint;
    private int trayTileMidpoint;
    private int draggingTileMidpoint;
 private boolean readyToDraw = false;
    private long dblTapCheck = 0;
    private boolean isMoving = false;
    private boolean isMomentum = false;
    private int previousY = 0;
    private int previousX = 0;
    private int previousTouchMotion = -3;
    private int currentTouchMotion = -3;
    private boolean alreadyInZoomedState = false;
  private int height;
    private SurfaceHolder holder;
    
  //  private Game game;
    //private GameTile targetTile = null;
    //private GameTile draggingTile = null;
    private int draggingTileId = -1;
    private int targetTileId = -1;
    private int currentTrayTileId = -1;
    private int dropTargetTrayTileId = -1;
    private boolean isBoardTileDragging(){
    	return this.getDraggingTile() != null;
    } 
    
    private boolean isTrayTileDragging(){
    	if (this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()){
    		return true;
    	}
    	return false;
    }
    
    private boolean isTrayTileDraggingButNotMovedYet(){
    	if (this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()){
    		return !this.getCurrentTrayTile().hasTileBeenMoved();
    	}
    	return false;
    }
    
    private GameTile getDraggingTile(){
    	if (this.draggingTileId > -1){
    		return this.tiles.get(this.draggingTileId);
    	}
    	
    	return null;
    }
    
    private void clearDraggingTile(){
    	this.draggingTileId = -1;
    }
    
    private GameTile getTargetTile(){
    	if (this.targetTileId > -1){
    		return this.tiles.get(this.targetTileId);
    	}
    	
    	return null;
    }
    
    private void clearTargetTile(){
    	this.targetTileId = -1;
    }
    
    private TrayTile getCurrentTrayTile(){
    	if (this.currentTrayTileId > -1){
    		return this.trayTiles.get(this.currentTrayTileId);
    	}
    	
    	return null;
    }
    
    private void clearCurrentTrayTile(){
    	if (this.currentTrayTileId > -1) {this.trayTiles.get(this.currentTrayTileId).removeActiveDrag();}
    	this.currentTrayTileId = -1;
    }
    
    private TrayTile getDropTargetTrayTile(){
    	if (this.dropTargetTrayTileId > -1){
    		return this.trayTiles.get(this.dropTargetTrayTileId);
    	}
    	
    	return null;
    }
    
    private void clearDropTargetTrayTile(){
    	this.dropTargetTrayTileId = -1;
    }

    
    //private TrayTile currentTrayTile = null;
   /// private Bitmap trayBackground;
    private Bitmap logo;
    private boolean surfaceCreated = false;
 	private boolean shuffleRedraw = false;
	private boolean afterPlayRedraw = false;
	private boolean recallLettersRedraw = false;
	private boolean wordHintRedraw = false;

	
	/*
	private Bitmap bgTrayBaseScaled;
	private Bitmap bgTrayEmptyScaled;
	private Bitmap bgTrayBaseDragging; 
	private Bitmap bgPlacedTileFull;
	private Bitmap bgPlacedTileZoomed;
	private Bitmap bgPlayedTileFull;
	private Bitmap bgPlayedTileZoomed;
	private Bitmap bgLastPlayedTileFull;
	private Bitmap bgLastPlayedTileZoomed;
	private Bitmap bgBaseScaled;
	private Bitmap bgBaseZoomed;
	private Bitmap bg4LScaled;
	private Bitmap bg4LZoomed;
	private Bitmap bg3LScaled;
	private Bitmap bg3LZoomed;
	private Bitmap bg3WScaled;
	private Bitmap bg3WZoomed;
	private Bitmap bg2LScaled;
	private Bitmap bg2LZoomed; 
	private Bitmap bg2WScaled;
	private Bitmap bg2WZoomed;	
	private Bitmap bgStarterScaled;
	private Bitmap bgStarterZoomed;
	*/
	
	public boolean isReadyToDraw() {
		return readyToDraw;
	}

	public void setReadyToDraw(boolean readyToDraw) {
		this.readyToDraw = readyToDraw;
	}

	List<Coordinate> coordinates = new ArrayList<Coordinate>();
	List<GameTile> tiles = new ArrayList<GameTile>();
	List<TrayTile> trayTiles = new ArrayList<TrayTile>();
    private TileLayout defaultLayout;
    //TileLayoutService layoutService;
   // AlphabetService alphabetService;
  //  WordService wordService;
   

 
//	public Game getGame() {
//		return game;
//	}

//	public void setGame(Game game) {
//		this.game = game;
//	}

	public GameSurfaceView(Context context) {
		super(context);
		//this.construct(context);
	 
	}

	public List<GameTile> getTiles() {
		return tiles;
	}

	public void setTiles(List<GameTile> tiles) {
		this.tiles = tiles;
	}

 

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.construct(context);
		 
		
		// TODO Auto-generated constructor stub
	}

	public void construct( GameSurface parent) {
		//Logger.w(TAG, "construct called");
		this.parent = parent;
		//this.appContext = appContext;
		this.parent.captureTime(TAG + " construct starting");
		
		//this.context = context;
		//this.layoutService = new TileLayoutService();
		this.defaultLayout = TileLayoutService.getDefaultLayout(parent); // layoutService.GetDefaultLayout(parent);
		
	//	this.alphabetService = alphabetService; //new AlphabetService(context);
		//this.wordService = wordService;
		//
		  this.setZOrderOnTop(true);
		 this.holder = getHolder();
		 this.holder.addCallback(this);
		 this.gameThread = new GameThread(holder, this);
		
		 setFocusable(true);
		 //this.bonusTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_BOARD_FONT);
		 //this.letterTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_LETTER_FONT); 
		 //this.letterValueTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_LETTER_VALUE_FONT); 
		 this.holder.setFormat(PixelFormat.TRANSPARENT);// necessary
		 
		 isTablet = getResources().getBoolean(R.bool.isTablet);
		 if (isTablet) {
			 this.zoomMultiplier = 1.5f;
		      //this.isZoomAllowed = false;
		 }  
  		 if (this.parent.getResources().getInteger(R.integer.derived_device_screen_size) == Constants.SCREEN_SIZE_XLARGE || 
  		  		 this.parent.getResources().getInteger(R.integer.derived_device_screen_size) == Constants.SCREEN_SIZE_LARGE){
			 this.autoZoom = false;
		 }
		// this.isDrawn = false;
		 
		// this.setScrollContainer(true);
	///	 this.setVerticalScrollBarEnabled(true);
	//	 this.setHorizontalScrollBarEnabled(true);
		 		 
		 this.post(new Runnable() 
		    {   
		        @Override
		        public void run() {

		    		me.parent.captureTime(TAG + " runnable starting"); 	
		   		me.SetDerivedValues();
				me.parent.captureTime(TAG + " setDerivedValues ended");
		   	   // me.LoadTiles();
		   	   // me.LoadTray();
		   	    me.LoadExtras();
		   	    Logger.w(TAG, "run called");
		   	    
		   	    
		  
		   	     LayoutParams lp = me.getLayoutParams();
			 	  lp.height = me.height;
			 	  
				  me.parent.captureTime(TAG + " load game starting");
			 	  me.loadGame();
				  me.parent.captureTime(TAG + " load game ended");
			//	  // Apply to new dimension
			 	  me.setLayoutParams( lp );
			 //	  me.setInitialRecallShuffleState();
			//	  me.resetPointsView();
			 	  me.readyToDraw = true;
		        }

		     });
		 
			this.parent.captureTime(TAG + " construct ended");
	}

	public void loadGame(){
		Logger.d(TAG,"loadGame game turn=" + this.parent.getGame().getTurn());
		
		this.LoadTiles();
	   	this.LoadTray();
	    this.setInitialButtonStates();
	    this.resetPointsView();
	    this.parent.callback();
	}

	
	private void SetDerivedValues(){
		me.getLayoutParams();
 
		int[] coordinates = new int[2];
		this.getLocationOnScreen(coordinates);
		
		this.absoluteTop = coordinates[1];
		this.absoluteLeft = coordinates[0];
		
	// 	Display display = this.parent.getWindowManager().getDefaultDisplay(); 
	// 	int w = display.getWidth();  // deprecated
	// 	int h = this.getHolder().getSurfaceFrame().bottom ; //display.getHeight();  // deprecated
		 
	 	//this.getHolder().getSurfaceFrame().bottom;
	 //	Logger.d(TAG, "SetDerivedValues getSurfaceFrame().bottom=" +  this.getHolder().getSurfaceFrame().bottom );
	 //	Logger.d(TAG, "SetDerivedValues getSurfaceFrame().top=" +  this.getHolder().getSurfaceFrame().top );
	 //	Logger.d(TAG, "SetDerivedValues getSurfaceFrame().left=" +  this.getHolder().getSurfaceFrame().left );
	 //	Logger.d(TAG, "SetDerivedValues getSurfaceFrame().right=" +  this.getHolder().getSurfaceFrame().right );
	 	
	 	DisplayMetrics displaymetrics = new DisplayMetrics();
	 	this.parent.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    //	int w  = displaymetrics.widthPixels;
	//  	Logger.d(TAG, "SetDerivedValues gameboard_button_area_height=" +  this.parent.getResources().getInteger(com.riotapps.word.R.integer.gameboard_button_area_height) );
//	 	Logger.d(TAG, "SetDerivedValues scoreboard_height=" +  this.parent.getResources().getInteger(com.riotapps.word.R.integer.scoreboard_height) );
//	  	Logger.d(TAG, "SetDerivedValues h1=" +  this.getHeight() );
//	  	 Logger.d(TAG, "SetDerivedValues h=" +  h );
	 	 this.fullWidth = this.getWidth();
	 	 this.movementTriggerTapcheckThreshold = this.fullWidth * this.movementTriggerTapcheckThresholdPercentageOfTotalWidth;
	//	 this.height = h -  this.parent.getResources().getInteger(com.riotapps.word.R.integer.gameboard_button_area_height) - this.parent.getResources().getInteger(com.riotapps.word.R.integer.scoreboard_height);// + 6;// lp.height; //getMeasuredHeight();

	 	 //	 this.height = this.getHeight() - this.parent.getResources().getInteger(com.riotapps.word.R.integer.gameboard_button_area_height) - this.parent.getResources().getInteger(com.riotapps.word.R.integer.scoreboard_height);// + 6;// lp.height; //getMeasuredHeight();

	 	 this.parent.captureTime("SetDerivedValues before getHeight");		
	 	  this.height = this.getHeight() - 
		 		 Utils.convertDensityPixelsToPixels(parent, this.parent.getResources().getInteger(com.riotapps.wordbase.R.integer.gameboard_button_area_height));// - 
		 		// Utils.convertDensityPixelsToPixels(context, this.parent.getResources().getInteger(com.riotapps.word.R.integer.scoreboard_height));// + 6;// lp.height; //getMeasuredHeight();

	//	 Logger.d(TAG, "SetDerivedValues  this.height=" +  this.height );
		 //	 this.height = this.getHeight() - GameSurface.BUTTON_CONTROL_HEIGHT - GameSurface.SCOREBOARD_HEIGHT + 6;// lp.height; //getMeasuredHeight();
	 //	this.height = this.parent.getWindowHeight() - GameSurface.SCOREBOARD_HEIGHT - GameSurface.BUTTON_CONTROL_HEIGHT-100;

	 	this.parent.captureTime("SetDerivedValues before math");
	 	this.trayTileSize = Math.round(this.fullWidth / 7.50f);	
	 	int maxTrayTileSize = this.parent.getResources().getInteger(R.integer.maxTrayTileSize);
	 	int maxDraggingTileSize = this.parent.getResources().getInteger(R.integer.maxDraggingTileSize);
		if (this.trayTileSize > maxTrayTileSize){this.trayTileSize = maxTrayTileSize;}
		this.draggingTileSize  = Math.round(this.trayTileSize * 1.6f);
		if (this.draggingTileSize > maxDraggingTileSize){this.draggingTileSize = maxDraggingTileSize;}
		this.trayTileLeftMargin = Math.round(this.fullWidth - ((this.trayTileSize * 7) + (TRAY_TILE_GAP * 6))) / 2;
	 	this.trayTop = this.height - trayTileSize -  TRAY_VERTICAL_MARGIN; 
		this.bottomOfFullView = this.trayTop - TRAY_VERTICAL_MARGIN - TRAY_TOP_BORDER_HEIGHT - 1;
		this.topGapHeight = Math.round((this.bottomOfFullView - this.fullWidth) / 2);
		this.bottomGapHeight = this.bottomOfFullView - this.fullWidth -  this.topGapHeight;
	 	 
		this.fullViewTileWidth = Math.round(this.fullWidth/15) - this.tileGap; //-1 for the space between each tile
		
	
		
		this.excessWidth = this.fullWidth - ((this.fullViewTileWidth * 15) + (14 * this.tileGap));
		this.zoomedTileWidth = Math.round(this.fullViewTileWidth * this.zoomMultiplier);
		this.midpoint = Math.round(this.fullWidth / 2);
		this.outerZoomLeft = this.fullWidth - Math.round((this.zoomedTileWidth + 1) * 15); 
	//	this.outerZoomTop = this.trayTop - TRAY_TOP_BORDER_HEIGHT - 1 - Math.round((this.zoomedTileWidth + 1) * 15); ///((this.fullViewTileWidth + 1) * 15) - Math.round((this.zoomedTileWidth + 1) * 15);  
//		this.outerZoomTop = ((this.fullViewTileWidth + 1) * 15) - Math.round((this.zoomedTileWidth + 1) * 15) + this.topGapHeight + this.bottomGapHeight + 1;  

		this.fullViewTileMidpoint = Math.round(this.fullViewTileWidth / 2);
		this.zoomedTileMidpoint = Math.round(this.zoomedTileWidth / 2);	
		this.trayTileMidpoint = Math.round(this.trayTileSize / 2);	
		this.draggingTileMidpoint = Math.round(this.draggingTileSize / 2);	
		this.trayHeight = this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2);

		this.parent.captureTime("SetDerivedValues before bitmaps");
		  
		
		 if (GameSurfaceView.bgTrayBaseScaled == null) {
				//GameSurfaceView.bgTrayBaseScaled = BitmapFactory.decodeResource(getResources(), R.drawable.tray_tile_bg);//ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tray_tile_bg, this.trayTileSize,this.trayTileSize);
				
			GameSurfaceView.bgTrayBaseScaled = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tray_tile_bg, this.trayTileSize,this.trayTileSize);
			//GameSurfaceView.bgTrayBaseScaled = Bitmap.createScaledBitmap(GameSurfaceView.bgTrayBaseScaled, this.trayTileSize , this.trayTileSize, false);
 
			 GameSurfaceView.bgTrayBaseScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bgTrayBaseScaled, this.trayTileSize, this.trayTileSize); 			 
		 }
		 this.parent.captureTime("SetDerivedValues bgTrayBaseDragging start load resized");
		 if (GameSurfaceView.bgTrayBaseDragging == null) {
		 	GameSurfaceView.bgTrayBaseDragging = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tray_tile_bg, this.draggingTileSize,this.draggingTileSize);
		//	GameSurfaceView.bgTrayBaseDragging = Bitmap.createScaledBitmap(GameSurfaceView.bgTrayBaseDragging, this.draggingTileSize , this.draggingTileSize, false);

			///GameSurfaceView.bgTrayBaseDragging = BitmapFactory.decodeResource(getResources(), R.drawable.tray_tile_bg);
			// GameSurfaceView.bgTrayBaseDragging = Bitmap.createScaledBitmap(GameSurfaceView.bgTrayBaseDragging, this.draggingTileSize , this.draggingTileSize, false);

			  GameSurfaceView.bgTrayBaseDragging = ImageHelper.getResizedBitmap(GameSurfaceView.bgTrayBaseDragging, this.draggingTileSize, this.draggingTileSize);		
	 	 }	 
		 this.parent.captureTime("SetDerivedValues bgTrayBackground start load resized");
	/*  if (GameSurfaceView.bgTrayBackground == null) {
			// GameSurfaceView.bgTrayBackground = BitmapFactory.decodeResource(getResources(), R.drawable.sbd_bg);
			 GameSurfaceView.bgTrayBackground = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.gameboard_tray_bg, this.fullWidth,this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2));
			//  GameSurfaceView.bgTrayBackground = Bitmap.createScaledBitmap(GameSurfaceView.bgTrayBackground, this.fullWidth, this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2), false);

			  GameSurfaceView.bgTrayBackground = ImageHelper.getResizedBitmap(GameSurfaceView.bgTrayBackground, this.fullWidth, this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2));
		 }
		*/ 
		 this.parent.captureTime("SetDerivedValues bgTrayEmptyScaled start load resized");
		 if (GameSurfaceView.bgTrayEmptyScaled == null) {
				//GameSurfaceView.bgTrayEmptyScaled = BitmapFactory.decodeResource(getResources(), R.drawable.tray_tile_empty_bg); //ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tray_tile_empty_bg, this.trayTileSize,this.trayTileSize);
				
			GameSurfaceView.bgTrayEmptyScaled = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tray_tile_empty_bg, this.trayTileSize,this.trayTileSize);
		 	//GameSurfaceView.bgTrayEmptyScaled = Bitmap.createScaledBitmap(GameSurfaceView.bgTrayEmptyScaled, this.trayTileSize , this.trayTileSize, false);
		
			 GameSurfaceView.bgTrayEmptyScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bgTrayEmptyScaled, this.trayTileSize, this.trayTileSize);
		 }
		 this.parent.captureTime("SetDerivedValues bgPlacedTileZoomed start load resized");
		
		 if (GameSurfaceView.bgPlacedTileZoomed == null){
			//GameSurfaceView.bgPlacedTileZoomed =  BitmapFactory.decodeResource(getResources(), R.drawable.tile_placed_bg); //ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_placed_bg, this.zoomedTileWidth,this.zoomedTileWidth);
			GameSurfaceView.bgPlacedTileZoomed =   ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_placed_bg, this.zoomedTileWidth,this.zoomedTileWidth);
			
			//GameSurfaceView.bgPlacedTileZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bgPlacedTileZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
			
			 GameSurfaceView.bgPlacedTileZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bgPlacedTileZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
		 }
		 this.parent.captureTime("SetDerivedValues bgPlacedTileFull start load resized");
		 if (GameSurfaceView.bgPlacedTileFull == null) {
			// GameSurfaceView.bgPlacedTileFull = Bitmap.createScaledBitmap(GameSurfaceView.bgPlacedTileZoomed, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
			
			  GameSurfaceView.bgPlacedTileFull = ImageHelper.getResizedBitmap(GameSurfaceView.bgPlacedTileZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1);
			 
		 } 
		 this.parent.captureTime("SetDerivedValues bgLastPlayedTileZoomed start load resized");
		 if (GameSurfaceView.bgLastPlayedTileZoomed == null) {
			//GameSurfaceView.bgLastPlayedTileZoomed = BitmapFactory.decodeResource(getResources(), R.drawable.tile_last_played_bg); //ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_last_played_bg, this.zoomedTileWidth,this.zoomedTileWidth);
			GameSurfaceView.bgLastPlayedTileZoomed = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_last_played_bg, this.zoomedTileWidth,this.zoomedTileWidth);

			//GameSurfaceView.bgLastPlayedTileZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bgLastPlayedTileZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
	 
			 GameSurfaceView.bgLastPlayedTileZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bgLastPlayedTileZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
		 }
		 this.parent.captureTime("SetDerivedValues bgLastPlayedTileFull start load");
		 if (GameSurfaceView.bgLastPlayedTileFull == null) {
			// GameSurfaceView.bgLastPlayedTileFull = Bitmap.createScaledBitmap(GameSurfaceView.bgLastPlayedTileZoomed, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
		 
			  GameSurfaceView.bgLastPlayedTileFull = ImageHelper.getResizedBitmap(GameSurfaceView.bgLastPlayedTileZoomed,  this.fullViewTileWidth + 1,  this.fullViewTileWidth + 1);
		 }
		 this.parent.captureTime("SetDerivedValues bgPlayedTileZoomed start load");
		 if (GameSurfaceView.bgPlayedTileZoomed == null) {
			//GameSurfaceView.bgPlayedTileZoomed =  BitmapFactory.decodeResource(getResources(), R.drawable.tile_played_bg); //ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_played_bg, this.zoomedTileWidth,this.zoomedTileWidth);
			GameSurfaceView.bgPlayedTileZoomed = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_played_bg, this.zoomedTileWidth,this.zoomedTileWidth);
			
			//GameSurfaceView.bgPlayedTileZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bgPlayedTileZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);

			 GameSurfaceView.bgPlayedTileZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bgPlayedTileZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1); 
			
		 }
	 
		 this.parent.captureTime("SetDerivedValues bgPlayedTileFull start load");
		 if (GameSurfaceView.bgPlayedTileFull == null) {
			// GameSurfaceView.bgPlayedTileFull = Bitmap.createScaledBitmap(GameSurfaceView.bgPlayedTileZoomed, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
			 
			 GameSurfaceView.bgPlayedTileFull = ImageHelper.getResizedBitmap(GameSurfaceView.bgPlayedTileZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1);
		 }
		 this.parent.captureTime("SetDerivedValues bgBaseZoomed end load");
		 this.trayAreaRect.setTop(this.trayTop - TRAY_VERTICAL_MARGIN - TRAY_TOP_BORDER_HEIGHT);
	     this.trayAreaRect.setBottom(this.trayTop + this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2));
	     this.trayAreaRect.setLeft(0);
	     this.trayAreaRect.setRight(this.fullWidth);
	     
		 this.visibleAreaRect.setTop(0);
	     this.visibleAreaRect.setBottom(this.height);
	     this.visibleAreaRect.setLeft(0);
	     this.visibleAreaRect.setRight(this.fullWidth);
	     
		 this.boardAreaRect.setTop(0);
	     this.boardAreaRect.setBottom(this.trayTop - 1); //setBottom(this.topGapHeight + this.fullWidth); //this.trayAreaRect.getTop() - 1);
	     this.boardAreaRect.setLeft(0);
	     this.boardAreaRect.setRight(this.fullWidth);
	     
			this.outerZoomTop =this.trayAreaRect.getTop() - Math.round((this.zoomedTileWidth + 1) * 15); ///((this.fullViewTileWidth + 1) * 15) - Math.round((this.zoomedTileWidth + 1) * 15);  
//			this.outerZoomTop = ((this.fullViewTileWidth + 1) * 15) - Math.round((this.zoomedTileWidth + 1) * 15) + this.topGapHeight + this.bottomGapHeight + 1;  

	     
//		 final BitmapFactory.Options boardTileOptions = new BitmapFactory.Options();
//		 boardTileOptions.inSampleSize = 
//				 ImageHelper.decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));
		 //Bitmap bgBase = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile_bg);
			this.parent.captureTime("SetDerivedValues bgBaseZoomed start load");
		if (GameSurfaceView.bgBaseZoomed == null) {
			//GameSurfaceView.bgBaseZoomed = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile_bg); //ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.blank_tile_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);

			GameSurfaceView.bgBaseZoomed = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.blank_tile_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
			//GameSurfaceView.bgBaseZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bgBaseZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);

			 GameSurfaceView.bgBaseZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bgBaseZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
		}
		this.parent.captureTime("SetDerivedValues bgBaseScaled start load");
		if (GameSurfaceView.bgBaseScaled == null) {
		 	//GameSurfaceView.bgBaseScaled = Bitmap.createScaledBitmap(GameSurfaceView.bgBaseZoomed, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);

			 GameSurfaceView.bgBaseScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bgBaseZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1);
		}
		this.parent.captureTime("SetDerivedValues bg4LZoomed start load");
		if (GameSurfaceView.bg4LZoomed == null) {
			//GameSurfaceView.bg4LZoomed = BitmapFactory.decodeResource(getResources(), R.drawable.tile_4l_bg); //ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_4l_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
			GameSurfaceView.bg4LZoomed =  ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_4l_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);

			//GameSurfaceView.bg4LZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bg4LZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);

			GameSurfaceView.bg4LZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bg4LZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
		}
		this.parent.captureTime("SetDerivedValues bg4LScaled start load");
		
		if (GameSurfaceView.bg4LScaled == null) {
			//GameSurfaceView.bg4LScaled = Bitmap.createScaledBitmap(GameSurfaceView.bg4LZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1, false);

			 GameSurfaceView.bg4LScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bg4LZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1);
		}
		 this.parent.captureTime("SetDerivedValues bg3LZoomed start load"); 
		 if (GameSurfaceView.bg3LZoomed == null){
		// Bitmap bg3L = BitmapFactory.decodeResource(getResources(), R.drawable.tile_3l_bg);
			 GameSurfaceView.bg3LZoomed = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_3l_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
			// GameSurfaceView.bg3LZoomed = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_3l_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);

			 // GameSurfaceView.bg3LZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bg3LZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 	 GameSurfaceView.bg3LZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bg3LZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
		 }
	//	 else {
	//		 this.bg3LZoomed = this.appContext.getBg3LZoomed();
	//	 }
		 this.parent.captureTime("SetDerivedValues bg3LScaleds start load");
		 if (GameSurfaceView.bg3LScaled == null){
			// GameSurfaceView.bg3LScaled = Bitmap.createScaledBitmap(GameSurfaceView.bg3LZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1, false);

			 GameSurfaceView.bg3LScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bg3LZoomed,this.fullViewTileWidth + 1,this.fullViewTileWidth + 1);
		 }
		 this.parent.captureTime("SetDerivedValues bg3WZoomed start load");
	//	 Bitmap bg3W = BitmapFactory.decodeResource(getResources(), R.drawable.tile_3w_bg);
		 if ( GameSurfaceView.bg3WZoomed == null){
			 GameSurfaceView.bg3WZoomed =   ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_3w_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
			// GameSurfaceView.bg3WZoomed =  BitmapFactory.decodeResource(getResources(), R.drawable.tile_3w_bg); //ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_3w_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
			//		 GameSurfaceView.bg3WZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bg3WZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);

			  GameSurfaceView.bg3WZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bg3WZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
		 }
		 this.parent.captureTime("SetDerivedValues bg3WScaled start load");
		 if (GameSurfaceView.bg3WScaled == null) {
			// GameSurfaceView.bg3WScaled = Bitmap.createScaledBitmap(GameSurfaceView.bg3WZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1, false);

			 GameSurfaceView.bg3WScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bg3WZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1);
		 }
//		 else {
//			 this.bg3WScaled = this.appContext.getBg3WScaled();
//		 }

		 this.parent.captureTime("SetDerivedValues bg2LZoomed start load");
		 if (GameSurfaceView.bg2LZoomed == null) {
			 GameSurfaceView.bg2LZoomed =  ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_2l_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
			// GameSurfaceView.bg2LZoomed =  BitmapFactory.decodeResource(getResources(), R.drawable.tile_2l_bg); //ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_2l_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
					//  GameSurfaceView.bg2LZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bg2LZoomed, this.zoomedTileWidth + 1 , this.zoomedTileWidth + 1, false);
		 
			  GameSurfaceView.bg2LZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bg2LZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
		 }
	 
 
		 if ( GameSurfaceView.bg2LScaled == null) {
			// GameSurfaceView.bg2LScaled = Bitmap.createScaledBitmap(GameSurfaceView.bg2LZoomed, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);

			  GameSurfaceView.bg2LScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bg2LZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1);
		 }
	 
		 this.parent.captureTime("SetDerivedValues bg2WZoomed start load");
		 if (GameSurfaceView.bg2WZoomed == null) {
			 //this.bg2WZoomed = BitmapFactory.decodeResource(getResources(), R.drawable.tile_2w_bg);
			// GameSurfaceView.bg2WZoomed = BitmapFactory.decodeResource(getResources(), R.drawable.tile_2w_bg); // ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_2w_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
			 GameSurfaceView.bg2WZoomed =  ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_2w_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);

			// GameSurfaceView.bg2WZoomed = Bitmap.createScaledBitmap(GameSurfaceView.bg2WZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
			 GameSurfaceView.bg2WZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bg2WZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
		 }
 
		 
		 if (GameSurfaceView.bg2WScaled == null) {
			// GameSurfaceView.bg2WScaled = Bitmap.createScaledBitmap( GameSurfaceView.bg2WZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1, false);
			
			 GameSurfaceView.bg2WScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bg2WZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1);
			 // this.appContext.setBg2WScaled(this.bg2WScaled);
		 }
//		 else {
//			 this.bg2WScaled = this.appContext.getBg2WScaled();
//		 }
		 this.parent.captureTime("SetDerivedValues bgStarterZoomed start load");
		 if (GameSurfaceView.bgStarterZoomed == null) {
			 GameSurfaceView.bgStarterZoomed =  ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_starter_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);

			 //GameSurfaceView.bgStarterZoomed = BitmapFactory.decodeResource(getResources(), R.drawable.tile_starter_bg);
			// this.bgStarterZoomed =  ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.tile_starter_bg, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1);
			 GameSurfaceView.bgStarterZoomed = ImageHelper.getResizedBitmap(GameSurfaceView.bgStarterZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1); 	
//			 GameSurfaceView.bgStarterZoomed = Bitmap.createScaledBitmap( GameSurfaceView.bgStarterZoomed, this.zoomedTileWidth + 1 , this.zoomedTileWidth + 1, false);
		 }
		// else {
		//	 this.bgStarterZoomed = this.appContext.getBgStarterZoomed();
		// }
		 
		 this.parent.captureTime("SetDerivedValues bgStarterScaled start load");
		 if (GameSurfaceView.bgStarterScaled == null) {	 
			 //GameSurfaceView.bgStarterScaled = Bitmap.createScaledBitmap( GameSurfaceView.bgStarterZoomed, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
	
			 GameSurfaceView.bgStarterScaled = ImageHelper.getResizedBitmap(GameSurfaceView.bgStarterZoomed, this.fullViewTileWidth + 1, this.fullViewTileWidth + 1); 	
			//	
			 // this.appContext.setBgStarterScaled(this.bgStarterScaled);
		 }
//		 else {
//			 this.bgStarterScaled = this.appContext.getBgStarterScaled();
//		 }
		
		 this.parent.captureTime("SetDerivedValues after bitmap loads");
		//Toast t = Toast.makeText(context, "Hello " +  this.height + " " + this.fullWidth + " " + getMeasuredHeight() , Toast.LENGTH_LONG);   
	    //t.show();
	}
	

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Logger.w(TAG, "surfaceChanged called");
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Logger.w(TAG, "surfaceCreated called");
		this.parent.captureTime("surfaceCreated starting");
		this.startThread();
		this.surfaceCreated = true;
		this.parent.captureTime("surfaceCreated ending");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Logger.w(TAG, "surfaceDestroyed called");
		if (this.parent != null) {
			this.parent.captureTime("surfaceDestroyed starting");
		}
	    this.stopThread();
	    this.surfaceCreated = false;
	    if (this.parent != null) {
	    	this.parent.captureTime("surfaceDestroyed ending");
	    }
	}
	
	private void dismissDialog(){
		if (this.dialog != null){
			dialog.dismiss();
			dialog = null;
		}
	}
	
	public void onPause() {
		Logger.w(TAG, "onPause called");
		this.dismissDialog();
		this.gameThread.onPause();
	//	 this.stopThread();
	}
	
	public void onStop() {
		Logger.w(TAG, "onStop called");
		this.dismissDialog();
		 this.stopThread();
	}
	
//	public void onBackPressed() {
//		Logger.w(TAG, "onBackPressed called");
//		 this.stopThread();
//	}
	
	public void onRestart() {
		 Logger.w(TAG, "onRestart called");
		//  if (this.surfaceCreated) {this.startThread();}
	//	this.gameThread = null;
		 this.gameThread = new GameThread(holder, this);
		 this.holder.setFormat(PixelFormat.TRANSPARENT);
	//	 this.startThread();
		 
		 me.readyToDraw = true;
	//	 this.startThread(); ///?????
	     if (this.surfaceCreated) {this.startThread();}
	}
	
	 public void onDestroy(){
		 Logger.w(TAG, "onDestroy called");
		this.surfaceDestroyed(this.surfaceHolder); 
		this.parent = null;
	 }
	
	 public void onWindowFocusChanged(){
		 Logger.w(TAG, "onWindowFocusChanged called");
		 this.stopThread();
		 }
	 
	public void onResume() {
		Logger.w(TAG, "onResume called");
		
		//make sure surface has been created first because onresume is initially called before surfacecreated and starting the 
		//thread then kills things (canvas is null in onDraw)
		this.parent.captureTime("startThread starting");
	 	if (this.surfaceCreated) {this.startThread();}
		this.parent.captureTime("startThread ended");
	}
	
	
	private void startThread(){
		
		Logger.w(TAG, "startThread called is thread alive " + this.gameThread.isAlive() + " isThreadRunning: " + this.isThreadRunning); 
		if (!this.isThreadRunning){ //() !=Thread.State.RUNNABLE) { 
			//if (!this.gameThread.isAlive()){
				this.gameThread.start();
				this.gameThread.setRunning(true);
				this.isThreadRunning = true;
			//}
		}
	//	else {
	//		this.gameThread.onResume();
	//	}
	}
	
	public void stopThreadLoop(){
	   
		this.readyToDraw = false;
		// this.gameThread.setRunning(false);
	}
	
	public void startThreadLoop(){
	    this.gameThread.setRunning(true);
	    this.gameThread.run();
	}
	
	private void stopThread(){
		// simply copied from sample application LunarLander:
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
		Logger.w(TAG, "stopThread called is thread alive " + this.gameThread.isAlive() + " isThreadRunning: " + this.isThreadRunning); 
		if (this.isThreadRunning){
	//		this.gameThread = null;
	//		this.isThreadRunning = false;
//		}
		
		    boolean retry = true;
		    this.gameThread.setRunning(false);
		    while (retry) {
		        try {
		        	this.gameThread.join();
		            retry = false;
		        } catch (InterruptedException e) {
		            // we will try it again and again...
		        }
		    }	
	    this.isThreadRunning = false;  
	    Logger.w(TAG, "stopThread is thread alive " + this.gameThread.isAlive());
		}
	}
	

	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
		 
		 if (this.parent == null){
			 this.readyToDraw = false;
			 return true;
		 }
			 
		 //if game is over, do not allow movement (or if game board has yet to be drawn the first time)
		 if (this.onDrawCounter == 0 || this.parent.getGame().getStatus() == 3 || this.parent.getGame().getStatus() == 4 ){
			 this.readyToDraw = false;
			 return true;
		 }
		 
		 //getX seems to be unreliable so lets use getRawX and convert to relative
	 //    int x = (int) event.getRawX();
	 //    int y = (int) event.getRawY();

	     //convert absolute coordinates to relative to this view
	     this.currentX = (int) event.getRawX() - this.absoluteLeft;
	     this.currentY = (int) event.getRawY() - this.absoluteTop;
	     
	     this.currentTouchMotion = event.getAction();
	     this.trayTileDropped = false;
	     this.trayTileDropTarget = false;
	     this.alreadyInZoomedState = false;
		// Logger.d(TAG, "onTouchEvent isSingleTap=off");	     
	     long currentTouchTime = System.currentTimeMillis(); //System.nanoTime(); 
	    
         Logger.w(TAG,  "onTouchEvent currentTime=" + currentTouchTime + " event=" + event.toString());
         Logger.w(TAG,  "onTouchEvent currentX=" + this.currentX + " currentY=" + this.currentY + " x=" + event.getX() + " y=" + event.getY() + " rawX=" + event.getRawX() + " rawY=" + event.getRawY());
 
         boolean setToReadyDraw = false;
    	 
	     synchronized (this.gameThread.getSurfaceHolder()) {
             switch (event.getAction()) {
             
             case MotionEvent.ACTION_DOWN:  ///0
            
            	 Logger.d(TAG, "onTouchEvent ACTION_DOWN");
            	 
                 //where is the click, which object within view???
            	 //get tile from coordinates.  if tile is null, do nothing
            	 //for now act like this is a click/tap...
            	 this.readyToDraw = false;
            	 this.tapCheck = currentTouchTime;
            	 
            	 Logger.d(TAG, "onTouchEVent  this.tapCheck=" +  this.tapCheck);
            	// if (this.dblTapCheck == 0){ this.dblTapCheck = currentTouchTime; }
            	// this.invalidate();
            	 this.isMoving = false; 
            	 
          
            	 this.previousX = this.currentX;
            	 this.previousY = this.currentY;
            	 this.coordinates.clear();
            	 this.isMomentum = false;
            	 
            	// GameTile targetTile = null;
            	 
            //	 if (!this.isZoomed){
            //		 targetTile = this.FindTileFromPositionInFullViewMode(this.currentX, this.currentY);
	         //    }
	          //   else{
	         //   	 targetTile = this.FindTileFromPositionInZoomedMode(this.currentX, this.currentY);
	          //   }
            	 
            	 //targetTile = this.findTapTargetTile(this.currentX, this.currentY);
            	 this.targetTileId = this.findTapTargetTile(this.currentX, this.currentY);
            	 
            	 
            	 //the user is starting to drag a tile that has already been placed
             	 //if (targetTile != null && targetTile.isDraggable()){
            	 if (this.getTargetTile() != null && this.getTargetTile().isDraggable()  && this.getTargetTile().getPlacedLetter().length() > 0){
            		 
            		 Logger.d(TAG, "onTouch action_DOWN targetTile.isDraggable"); ///xxxx
            		 //targetTile.setDraggingLetter(targetTile.getPlacedLetter());
            		 //targetTile.removePlacement();
            		 //targetTile.setDragging(true);
            		 //targetTile.setDrag();
            	 
            		 //this.tiles.get(targetTile.getId()).setDrag();
            		 this.draggingTileId = this.targetTileId;// targetTile.getId();
            		 
            		 //let's mark this tile as pending dragging.  we need to determine that a move is coming or a tap
            		 //if it's a tap, we don't want to start dragging yet
            		 this.getDraggingTile().setDragPending(true);
            		 Logger.w(TAG, "onTouch ACTION_DOWN setDragPending letter from dragTile  id=" + this.draggingTileId + " letter=" + this.getDraggingTile().getPlacedLetter() + " targetTile letter=" + this.getTargetTile().getPlacedLetter());
            		 //this.getDraggingTile().setDrag();
            		 
            		 //this.draggingTile = this.tiles.get(targetTile.getId());
            		 //this.getDraggingTile().setDraggingLetter(this.targetTile.getDisplayLetter());
            		 //this.getDraggingTile().setDragging(true);
            		 //targetTile = null;
            		// this.targetTile = null;
            		 
            		 Logger.d(TAG, "onTouch ACTION_DOWN this.draggingTile=" + this.getDraggingTile().getId() + " dragletter=" + this.getDraggingTile().getDraggingLetter());
            	 }
            	 //player has tapped a board tile
            	 //else if (targetTile != null){
            	 else if (this.getTargetTile() != null){
            		// this.targetTile = targetTile;
            		 
            		 Logger.d(TAG, "onTouch ACTION_DOWN targetTileId=" + this.getTargetTile().getId());
            	 }
            	 else if (this.getTargetTile() == null){
            		 //this.targetTile = null;
            		 //this.targetTile = targetTile;
            		 //TrayTile newTrayTile = this.FindTrayTileFromPosition(this.currentX, this.currentY);
            		 this.currentTrayTileId = this.FindTrayTileFromPosition(this.currentX, this.currentY);
            		 //this.currentTrayTile = this.FindTrayTileFromPositionInFullViewMode(this.currentX, this.currentY);
 
            		  Logger.d(TAG, "onTouch ACTION_DOWN currentTrayTileId=" + this.currentTrayTileId);
            		 
               		 if ( this.getCurrentTrayTile() != null){
               			 Logger.d(TAG, "onTouch ACTION_DOWN currentTrayTile- letter=" + this.getCurrentTrayTile().getCurrentLetter() + " dragging="+ this.getCurrentTrayTile().isDragging());
               			 //see if this tray tile is already being dragged.  if so the player is likely keeping his finger pressed in the down state
                		 //without moving it.  if so, do not redraw, the baord is already in correct state
               			//if (this.currentTrayTile != null && newTrayTile.getId() == this.currentTrayTile.getId() && this.currentTrayTile.isDragging()){
               			 if (this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()){
               				 this.readyToDraw = false;
	            		 }
	            		 else{ 
	            			// this.currentTrayTile = newTrayTile;
	        			 
	            			 Logger.d(TAG, "onTouch ACTION_DOWN currentTrayTile letter=" + this.getCurrentTrayTile().getCurrentLetter());
	            			 //if (this.currentTrayTile.getCurrentLetter().length() == 0){
		            		 if (this.getCurrentTrayTile().isEmpty()){
		            			//this tile is not draggable because it has no letter...so clear out tray tile and do nothing
		            			 this.clearCurrentTrayTile();
		            			 //this.currentTrayTile = null;
		            			 this.readyToDraw = false;
		            		 }
		            		 else{
		            			 Logger.d(TAG, "onTouch ACTION_DOWN currentTrayTile letter before loop=" + this.getCurrentTrayTile().getCurrentLetter());
		            			 //set this tray tile up to drag, loop through all tray tiles just to make sure no others are set for drag
		            			 for(TrayTile tile : this.trayTiles){
		            				 if (tile.getId() == this.currentTrayTileId){
		            					 tile.setUpForDrag();
		            				 }
		            				 else{
		            					 tile.removeActiveDrag();
		            				 }
		            			 }
		            			 Logger.d(TAG, "onTouch ACTION_DOWN currentTrayTile letter after loop=" + this.getCurrentTrayTile().getCurrentLetter());

		            			 Logger.d(TAG, "onTouchEvent cur tile.isdragging" + this.getCurrentTrayTile().isDragging());
		            			 //this.readyToDraw = true;
		            			 this.setReadyToDrawFromOnTouch(currentTouchTime);
		            		 }
	            		 }
            		 }
            	 }
            	 else{
            		 this.clearCurrentTrayTile();
            	 }
            	// this.currentTouchMotion = MotionEvent.ACTION_DOWN;
            	// return false; //??
            	  break;

             case MotionEvent.ACTION_UP: //1
            	 
            	
            	 //includes a check to ignore double taps
            	 //  Logger.w(getClass().getSimpleName() + "onTouchEvent ActionUP ", this.tapCheck + " " + currentTouchTime + " " + this.readyToDraw);
            	 this.parent.captureTime(TAG + " ACTION_UP");
            	   
            	   this.readyToDraw = false;
            	   Logger.w(TAG, "ACTION_UP double tapcheck = " + this.tapCheck + " currentTouchTime=" + currentTouchTime + " dbltapcheck = " + this.dblTapCheck + " diff = " + (this.tapCheck - this.dblTapCheck)); 
               	   Logger.w(TAG, "ACTION_UP single tapcheck = " + this.tapCheck + " SINGLE_TAP_DURATION_IN_MILLISECONDS=" + SINGLE_TAP_DURATION_IN_MILLISECONDS + " currentTouchTime=" + currentTouchTime + " tapcheck = " + this.tapCheck + " diff = " + (currentTouchTime - this.tapCheck)); 
     	   
            	 if (this.tapCheck > 0 && currentTouchTime - this.tapCheck <= SINGLE_TAP_DURATION_IN_MILLISECONDS) { 
            		 
            		 Logger.d(TAG, "onTouchEvent past first tap check");
            	 // && (currentTouchTime - this.dblTapCheck >= 800000000 || this.dblTapCheck == 0)) {
            	//	 if (this.isMoving){
            	//		 //if we are coming out of a drag, up event just means drag is finished, nothing to do here, just move along
            	//		 this.isMoving = false;
             	
            	//	 }
            		// else {// if (this.currentTouchMotion == MotionEvent.ACTION_DOWN){ //action up should immediately follow an action down to be a tap
	            	
            		 //first make sure action up event is not associated with move event
            		 if (!this.isMoving) {  

                		 Logger.d(TAG, "onTouchEvent not moving");

                		 //let's check here to see if a tray tile was tapped but not moved at all.
                		 //if we don't check for this scenario, the tray tile stays in pre-drag state and does not 
                		 //get set back into its original position after the finger is lifted
                		 if (this.isTrayTileDraggingButNotMovedYet()){
                			 this.getCurrentTrayTile().removeDrag();
                			 this.clearCurrentTrayTile();
                			 this.trayTileTapped = true;
                			 //this.readyToDraw = true;
                			 setToReadyDraw = true;
                		 }
            			 //check for a single tap
            			 //and make sure to ignore double tap events
                		 else if((this.tapCheck - this.dblTapCheck) >= (DOUBLE_TAP_DURATION_IN_MILLISECONDS - SINGLE_TAP_DURATION_IN_MILLISECONDS) || this.dblTapCheck == 0){
            				 
                    		 Logger.d(TAG, "onTouchEvent not a double tap");

            	
            				 //Logger.d(TAG,"this.targetTile null? " + this.targetTile == null);
            				 
            				 if (this.getTargetTile() == null){
            					 Logger.d(TAG, "onTouchEvent ACTION_UP this.targetTile == null");
            					 //this is a single tap situation outside of the game board.  do nothing
            					 this.readyToDraw = false;
            					 
            					 //clear out tiles
            					// this.targetTile = null;
            					// this.currentTrayTile = null;
            				 }
            				 else {
            					 //a tile was tapped on, at this precise moment the finger is pulled off the tapped tile
            					 Logger.d(TAG, "onTouchEvent ACTION_UP this.targetTile != null");
            					 
            					 //a tile was tapped on that contains a placed letter.  the single tap function overrides the drag function
            					 //so cancel the pending drag function
            					 if (this.getDraggingTile() != null && this.getDraggingTile().isDragPending()){
            						 Logger.d(TAG, "onTouchEvent ACTION_UP this.draggingTile is pending dragging");
            					
            						 this.clearTargetTile();
            						 this.getDraggingTile().setDragPending(false);
            						 this.clearDraggingTile();
            					 }
            					 this.isZoomed = !this.isZoomed;
            					 setToReadyDraw = true;
            					 
            				 }
            				 Logger.d(TAG, "onTouchEvent isSingleTap=true");
            				 this.dblTapCheck = currentTouchTime;
            			 }
            			 else {
            				 //restart double click check
            				 this.dblTapCheck = 0;
            			 }
            			 
            		 }
            		 
            		// else {
            		//	 //if previous action was not a down, don't draw
            		//	 this.readyToDraw = false;
            		// }
            	 }
            	 else {
            		 //we are coming out of a move action here...let's determine if the momentum scroll should be triggered
        			 //Logger.w(TAG,"onTouchEvent: ACTION_UP number of coordinates" + this.coordinates.size());
            	//	 this.parent.captureTime(TAG + " find tile starting");
        			 //first check to see if a tray tile is being dragged. if so this means the tray tile is being dropped
            		 //if ((this.currentTrayTile != null && this.currentTrayTile.isDragging()) || this.getDraggingTile() != null){
            		 
            		 //tile has starting being dragged but has not been drawn dragging yet.  let's just put this tile back in its former position
            		 //without this check, the letter was being flicked into the a position near the top left
            		 //because the dragging positions has never been set
            		 if (this.getDraggingTile() != null && !this.getDraggingTile().isDragPositionSet()){
            			 Logger.d(TAG, "drag is being removed because it never really got established.");
            			 this.getDraggingTile().removeDrag();
            			 this.clearDraggingTile();
            			 setToReadyDraw = true;
            		 } 
            		 else if ((this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()) || this.getDraggingTile() != null){
        				 Logger.d(TAG, "ACTION_UP a tile is dragging");
        				 //find out where we are on the board.
        				 //we need to determine if the tray tile was dropped 
        				 //if we are in full view mode (!zoomed) we need to determine if tile was dropped on the neutral areas
        				 //above and below board (upper and lower gaps).  If tile was dropped there we'll need to do more work to determine
        				 //what the closed eligible drop candidate was (either on a board tile or on the tray)
        				 //but first let's start with determining (the easy way) if we dropped on a board tile 

        			//	 this.parent.captureTime(TAG + " setClosestDropTarget starting");
        				 //we need to find where the tile was dropped.  because it was dropped somewhere.  this method will 
        				 //determine the closest available drop target to the dropped location (hence the name)
        				 this.setClosestDropTarget();
        				 
        		//		 this.parent.captureTime(TAG + " setClosestDropTarget ended");
        				// this.targetTileId = this.findClosestOpenTile(this.currentX, this.currentY);
        				
        				 if (this.getTargetTile() != null){
    						
        					 Logger.d(TAG, "ACTION_UP droptargetId=" + this.getTargetTile().getId() + " placetext length=" + this.getTargetTile().getPlacedLetter().length());
        					 
        					 //we have a match for the drop target!!  However the tile is not eligible to be dropped on if it already has a placed letter
        					 //from the tray.  if this is the case, let's find the closest eligible board tile from the drop target to drop the tile on.
        					 //if we cannot find a suitable drop target, return the tile to the tray
        					  
        						 //assign the letter from the tray tile to the board tile
        						// this.tiles.get(this.targetTile.getId()).setPlacedLetter(this.currentTrayTile.getDraggingLetter());
        					 
        					 this.parent.captureTime(TAG + " clearCurrentTrayTile starting");
        					 
        					 //handle a tray tile dropping
        					 if (this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()){
	    						 this.getTargetTile().setPlacedLetter(this.getCurrentTrayTile().getDraggingLetter());
	    						 this.getCurrentTrayTile().setCurrentLetter("");
	    						 //clear the active tiles
	    						// this.targetTile = null;
	    						 //this.currentTrayTile = null;		 
	    						 this.clearCurrentTrayTile();
        					 }
        					 //handle board tile dropping
        					 else if(this.getDraggingTile() != null){
        						 this.getTargetTile().setPlacedLetter(this.getDraggingTile().getDraggingLetter());
	    						 this.getDraggingTile().setDraggingLetter("");
	    						 this.clearDraggingTile(); //this.draggingTile = null;		 
        					 }
        					 
        					 this.parent.captureTime(TAG + " setGameState starting");
        					 
        					 this.parent.getGameState().resetLettersFromCurrent(tiles, trayTiles);
        					/// GameStateService.setGameState(this.context, this.parent.getGameState());
        					// this.resetPointsView();
    						 
        					 this.parent.captureTime(TAG + " setGameState ended");
        					 
    						 //let's make sure the board zooms (if it's not already in that state, upon drop)
        					 if(this.isZoomAllowed){
	    						 if (this.isZoomed){
	    							 //within onDraw, we will not move the board when dropping on board that is already in zoomed state
	    							 this.alreadyInZoomedState = true;
	    						 }
	    						 else{
	    							 if (this.autoZoom) { 
	    								 this.isZoomed = true; 
	    								 Logger.d(TAG, "ACTION_UP this.isZoomed = true=" + this.autoZoom);

	    							 }
	    							 else {
	    								 Logger.d(TAG, "ACTION_UP nt autoazoomed=" + this.autoZoom);
	    								 
	    							 
	    							 }
	    						 }
        					 }
    						// Logger.d(TAG, "ACTION_UP this.alreadyInZoomedState=" + this.alreadyInZoomedState);
    						 this.trayTileDropped = true;
    						 //this.readyToDraw = true;
    						 
    						 this.parent.captureTime(TAG + " resetPointsView starting");
    						 
    						 this.resetPointsView();
    						 setToReadyDraw = true;
    						 
    						 this.parent.captureTime(TAG + " resetPointsView ended");
    						 
    					 }
        				 else
        				 {
        					 //find out if drop occurred on tray tile!!!
        					 //TrayTile trayDropTargetTile = this.findClosestTrayTile(this.currentX, this.currentY);
        					 //for this use the center of the dragging tile, just in case the user's finger is past the 
        					 //bounds of the tray and the dragging tile has reached the bounds
        				
        					 //	 this.dropTargetTrayTileId = this.findClosestTrayTile();
        					 
        					 //match found!!  let's drop the target letter on the tray tile and shift
        					 //any letter (and subsequent letters) that might already be there
        					 //if (trayDropTargetTile !=null){
        					 if (this.getDropTargetTrayTile() !=null){
        						 //this.handleDropOnTray(trayDropTargetTile, this.currentTrayTile);
        						 this.handleDropOnTray();
        						 this.parent.getGameState().resetLettersFromCurrent(tiles, trayTiles);
        						/// GameStateService.setGameState(this.context, this.parent.getGameState());
        						 //this.readyToDraw = true;
        						 setToReadyDraw = true;
        						 this.trayTileDropTarget = true;
        					 }
        							 
        				 }
        					 
    					 //if current tile is still null, return dragged tile to its origin
    					 if (this.getTargetTile() == null){
    						 //if its a tray tile that is dragging return it to its previous state
    						 //if (this.currentTrayTile != null && this.currentTrayTile.isDragging()){
    						//	 this.currentTrayTile.removeDrag();
    						//	 this.currentTrayTile = null;
    						// }
    						 if (this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()){
    							 this.getCurrentTrayTile().removeDrag();
    							 this.clearCurrentTrayTile();
    						 }
    						 else if(this.getDraggingTile() != null){
    							 this.getDraggingTile().removeDrag();
    							 this.clearDraggingTile(); //this.draggingTile = null;
    						 }
    					 }
        			 }
        			 else{
        			 
	    				 //if we are coming out of a board move and we have at least 30 coordinates captured by move, let's trigger momentum scrolling
	    				 if (this.coordinates.size() == NUMBER_OF_COORDINATES_TO_TRIGGER_MOMENTUM_SCROLLING){
	    					 //if distance is too short that means the finger was in place and not moving and in this case
	    					 //do not trigger momentum scroll
	    					 
	    					 int previousX = Math.round(this.coordinates.get(0).getxLocation());
	    					 int currentX = Math.round(this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation());
	    					 int previousY = Math.round(this.coordinates.get(0).getyLocation());
	    					 int currentY = Math.round(this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation());
	    					 
	    					 Logger.w(TAG, "onTouchEvent: ACTION_UP previousX=" + previousX + " currentX=" + currentX + " diff=" + (currentX - previousX)); 
	    					 Logger.w(TAG, "onTouchEvent: ACTION_UP previousY=" + previousX + " currentY=" + currentY + " diff=" + (currentY - previousY)); 
	    					
	    					 //time since last move action
	    					// long timeSinceLastMoveInMilliseconds = (System.nanoTime() - this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getTimestamp()) / 1000000;
	    					 
	    					 long timeSinceLastMoveInMilliseconds = (System.currentTimeMillis() - this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getTimestamp()) ;
	    		    			
	    					 
	    					if (timeSinceLastMoveInMilliseconds < MOVE_STOPPED_DURATION_IN_MILLISECONDS) {
	    				//	if (currentX + Math.abs(currentX - previousX) >= currentX * (1 + MOVEMENT_TRIGGER_THRESHOLD) &&
	    				//		currentY + Math.abs(currentY - previousY) >= currentY * (1 + MOVEMENT_TRIGGER_THRESHOLD)) {	
	 //   					if (this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation() <= Math.round(this.coordinates.get(0).getxLocation() * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
	  //  						this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation() >= Math.round(this.coordinates.get(0).getxLocation() * (1 - MOVEMENT_TRIGGER_THRESHOLD)) && 
	   // 						this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation() <= Math.round(this.coordinates.get(0).getyLocation() * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
	   // 						this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation() >= Math.round(this.coordinates.get(0).getyLocation() * (1 - MOVEMENT_TRIGGER_THRESHOLD))){ 
	    				   // if (Math.abs(this.coordinates.get(0).getxLocation()) >= Math.abs(this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation() * (1 + MOVEMENT_TRIGGER_THRESHOLD)) &&
	    				   // 		Math.abs(this.coordinates.get(0).getyLocation()) >= Math.abs(this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation() * (1 + MOVEMENT_TRIGGER_THRESHOLD))){
	
		    					float speed = this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getTimestamp() - this.coordinates.get(0).getTimestamp();
		    					//speed = speed / 1000000; //convert to milliseconds	  
	
							 
							    int xDisplacement = this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation() - this.coordinates.get(0).getxLocation();
						          
						        int yDisplacement = this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation() - this.coordinates.get(0).getyLocation();
						        
						        Logger.w(TAG, "onTouchEvent: ACTION_UP speed " + speed);
						        Logger.w(TAG, "onTouchEvent: ACTION_UP xDisplacement " + xDisplacement + " yDisplacement " + yDisplacement);
						        
						        this.xPosition = tiles.get(0).getxPositionZoomed();
						        this.yPosition = tiles.get(0).getyPositionZoomed();    
						        
						        this.xDistance =  this.coordinates.get(0).getxLocation() - this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation(); ///multiply by speed to adjust
						        this.yDistance =  this.coordinates.get(0).getyLocation() - this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation(); ///multiply by speed to adjust;
							 
						        Logger.w(TAG, "onTouchEvent: ACTION_UP xDistance=" + this.xDistance + "  yDistance=" + this.yDistance + " speed=" + speed); 
						        
						        this.isMomentum = true;
						        setToReadyDraw = true;
		    					//this.readyToDraw = true;
	    				    }
	    					else {
	    						 Logger.w(TAG, "onTouchEvent: ACTION_UP threshold of movement not met to trigger momentum scrolling"); 
	    					}
	    				 }
    				 }
        		 
        		 }
            	 // this.currentTouchMotion = MotionEvent.ACTION_UP;
            	 this.previousX = 0;
            	 this.previousY = 0;
            	 this.isMoving = false;
            	 this.tapCheck = 0;
            	 if (setToReadyDraw) {this.setReadyToDrawFromOnTouch(currentTouchTime);}
            	 break;
             case MotionEvent.ACTION_MOVE:   
            	 
            	// this.currentTouchMotion = MotionEvent.ACTION_MOVE;
            	 //Logger.w(TAG, "ACTION_MOVE: x " + )
            //	 Logger.w(TAG, "ACTION_MOVE= tapCheck about to be set to 0");
            //	 this.tapCheck = 0;
            //	 this.dblTapCheck = 0;
            //	 this.isMoving = true;
            	 Logger.d(TAG, "ACTION MOVE  this.fullWidth=" +  this.fullWidth);
            	 Logger.w(TAG, "ACTION_MOVE= this.currentX=" + this.currentX + " this.previousX" + this.previousX + " this.currentY=" + this.currentY + " this.previousY" + this.previousY );

             	 Logger.w(TAG, "ACTION_MOVE  xDiff=" + Math.abs(this.currentX - this.previousX));
             	 Logger.w(TAG, "ACTION_MOVE  yDiff=" + Math.abs(this.currentY - this.previousY));
// Logger.w(TAG, "ACTION_MOVE  xPlus=" + Math.round(this.previousX * (1 + MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD)));
            	// Logger.w(TAG, "ACTION_MOVE  xMinus=" + Math.round(this.previousX * (1 - MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD)));
            	// Logger.w(TAG, "ACTION_MOVE  yPlus=" + Math.round(this.previousY * (1 + MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD)));
            	// Logger.w(TAG, "ACTION_MOVE  yMinus=" + Math.round(this.previousY * (1 - MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD)));

//            	 if (this.currentX <= Math.round(this.previousX * (1 + MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD)) ||
//      		   			  this.currentX >= Math.round(this.previousX * (1 - MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD)) || 
//      		   			  this.currentY <= Math.round(this.previousY * (1 + MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD)) || 
//      		   			  this.currentY >= Math.round(this.previousY * (1 - MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD))){

            	 
            	 //check for minimum threshold of movement  
            	 if (Math.abs(this.currentX - this.previousX) > this.movementTriggerTapcheckThreshold || //MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD ||
            		 Math.abs(this.currentY - this.previousY) > this.movementTriggerTapcheckThreshold){  //MOVEMENT_TRIGGER_TAPCHECK_THRESHOLD){
 
            		 //change this to span more than one move event because more sensitive devices are wiping out
            		 //tapcheck when move is not intended *** but will that affect the move logic below???
            		 //perhaps get tapcheck threshold based on percentage of screen size
            		 Logger.w(TAG, "ACTION_MOVE= minimum threshold met, tapCheck about to be set to 0");
            		 this.tapCheck = 0;
   		 			 this.dblTapCheck = 0;
   		 			 this.isMoving = true;
   		         	 
	            	 //check to see if a board tile is pending dragging. 
	            	 //if so, change pending state to dragging state  
	            	 if (this.getDraggingTile() != null && this.getDraggingTile().isDragPending()){
	            		 this.getDraggingTile().setDrag();
	            		  
	            		 Logger.w(TAG, "DRAG PENDING id=" + this.draggingTileId + " letter=" + this.getDraggingTile().getDraggingLetter());
	            	 }
	            	 //check to see if a board tile is being dragged
	            	 else if (this.getDraggingTile() != null){
	            		 //this.readyToDraw = true;
	            		 setToReadyDraw = true;
	            		 Logger.d(TAG, "getDraggingTile != null letter=" + this.getDraggingTile().getPlacedLetter());
	            		 
	            	 }
	            	 //check to see if a tray tile is being dragged
	            	 //else if (this.currentTrayTile != null && this.currentTrayTile.isDragging()){
	            	 else if (this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()){
	            		 //this.readyToDraw = true;
	            		 setToReadyDraw = true;
	            	 }
	            	 else{
		            	 if (!this.isZoomed){
		            		 this.readyToDraw = false;
		            	 }
		            	 //don't drag board outside of board area
		            	 else if (this.currentY > this.trayAreaRect.getTop()){ //this.trayAreaTop){
		            		 this.readyToDraw = false;
		            	 }
		            	 //use absolute values here????
		            	 //else if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && this.previousX == this.currentX && this.previousY == this.currentY){
		            	 else if (this.currentX <= Math.round(this.previousX * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
		            			  this.currentX >= Math.round(this.previousX * (1 - MOVEMENT_TRIGGER_THRESHOLD)) && 
		            			  this.currentY <= Math.round(this.previousY * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
		            			  this.currentY >= Math.round(this.previousY * (1 - MOVEMENT_TRIGGER_THRESHOLD))){
		            		 Logger.w(TAG,"onTouchEvent minimum threshold not met");
		            	 //else if (this.previousX == this.currentX && this.previousY == this.currentY){
		            		 this.readyToDraw = false;
		            	}
		            	else {
	
		            		 //this.readyToDraw = true;
		            		 
		            		 //keep latest 30 coordinates in context, last 10 will be used to calculate momentum and direction for scrolling
		            		 //having 30 determines if action_up triggers momentum scrolling logic
		            		 this.coordinates.add(new Coordinate(this.currentX, this.currentY, currentTouchTime));
		            		 //quick loop to remove first in coordinates over 30, normally should only ever remove one, but just in case, we'll loop it
		            		 while (this.coordinates.size() > NUMBER_OF_COORDINATES_TO_TRIGGER_MOMENTUM_SCROLLING){
		            			 this.coordinates.remove(0);
		            		 }
		            		 
		            		 setToReadyDraw = true;
		            		 Logger.w(TAG,"onTouchEvent: coodinates size" + this.coordinates.size());
		            	}
		            	// this.readyToDraw = false;
		            	 if (this.currentX < this.outerZoomLeft || this.currentY < this.outerZoomTop) {
		            		//calculate outerZoomRight and bottom
		            		
		            	 }
	            	 }
	            	 
	            	 if (setToReadyDraw) {
	            		 this.setReadyToDrawFromOnTouch(currentTouchTime);
	            	 }
            	 }
            	 else{
            		 Logger.d(TAG,"onTouchEvent minimum threshold NOT met for movement");
            	 }
            	 //  Logger.w(getClass().getSimpleName() + "onTouchEvent ACTION_MOVE ", this.previousX + " " + this.currentX + " " + this.readyToDraw);
            	 break; 
             default:
            	 this.readyToDraw = false;
             }
             //this.previousTouchMotion = this.currentTouchMotion;
         	 //this.previousTouchTime = currentTouchTime;
         	       			 
         	 this.parent.captureTime("ONTOUCH EVENT ENDED, ONDRAW SHOULD BE NEXT" );
         	 
             return true;
         }

	 }
	 
	 private void setReadyToDrawFromOnTouch(long currentTouchTime){
		 this.previousTouchMotion = this.currentTouchMotion;
     	 this.readyToDraw = true;
	 }

	 private void handleDropOnTray(){
		 
		 Logger.d(TAG, "handleDropOnTray called");
		 //shift letters as needed and reset GameState
		 //
		 //determine which type of tile is being dropped, a board tile or another tray tile
		 String dropLetter;
		 
		 //just in case the draggingTrayTile is being dropped back into it's own location
		 //if (draggingTrayTile != null && draggingTrayTile.getId() == dropTargetTile.getId()){
		 if (this.getCurrentTrayTile() != null && this.currentTrayTileId == this.dropTargetTrayTileId){
			 //just remove drag and we are back in business
			 //this.trayTiles.get(dropTargetTile.getId()).removeActiveDrag();
			 this.getDropTargetTrayTile().removeDrag();

			// draggingTile = null;
			// draggingTrayTile = null;
			 this.clearCurrentTrayTile();
			 this.clearDropTargetTrayTile(); //new
			 
			 Logger.d(TAG, "handleDropOnTray tray tile dropped back into its own location");
			 return;
		 }
		 
		 if (this.getDraggingTile() != null){
			 dropLetter = this.getDraggingTile().getDraggingLetter();
		 }
		 else{
			 dropLetter = this.getCurrentTrayTile().getDraggingLetter();
	//		 dropLetter = draggingTrayTile.getDraggingLetter();
		 }
	 
		 //if the target tray tile is already empty, just assign it a letter with no shifting
		 //if (dropTargetTile.isEmpty()){
		 if (this.getDropTargetTrayTile().isEmpty()){
			 //this.trayTiles.get(dropTargetTile.getId()).setCurrentLetter(dropLetter);
			 this.getDropTargetTrayTile().setCurrentLetter(dropLetter);
			 if (this.getDraggingTile() != null){
				// this.targetTile.removePlacement();
				 this.getDraggingTile().removeDragAndPlacement();
				 //draggingTile.removePlacement();
				 //this.tiles.get(draggingTile.getId()).removePlacement();
				 Logger.d(TAG, "handleDropOnTray  target tray tile is already empty ." + this.getDraggingTile().getDisplayLetter() + " - " + this.getDraggingTile().getPlacedLetter()); 
			 }
			 
			// draggingTile = null;
			 this.clearCurrentTrayTile();
			 this.clearDropTargetTrayTile(); 
			 this.clearDraggingTile();
			 this.clearTargetTile();
			 this.resetPointsView();
			 //draggingTrayTile = null;
			 return;
		 }
		 
		 //ok, based on the location of the closest empty tray tile in relation to the drop target
		 //let's determine the direction of the shift
		 //look to the left first, then look to the right
		 String loopLetter = dropLetter; //this.getDropTargetTrayTile().getCurrentLetter(); //dropTargetTile.getCurrentLetter();
		 int direction = 0;
		 int j = 1;
		 for(TrayTile tile : this.trayTiles){
			 if (tile.getId() != this.getDropTargetTrayTile().getId() && tile.isEmpty() && direction == 0){
				 if (tile.getId() < this.getDropTargetTrayTile().getId()) { direction = -1; } else { direction = 1; }
			 }
		 }
		 
		 //multiplying by direction will send the loop into the proper direction
		 j = j * direction;
		// Logger.d(TAG, "handleDropOnTray j=" + j);
	    int i = this.getDropTargetTrayTile().getId(); //+ (1 * direction);
	    
		// Logger.d(TAG, "handleDropOnTray dropTargetTile.getId()=" + this.getDropTargetTrayTile().getId() + " i=" + i);
	    boolean loopRun = true;
	    int escapeValue = 0;
	    while (loopRun == true) {
	    //	Logger.d(TAG, "handleDropOnTray currentLetter=" +  this.trayTiles.get(i).getCurrentLetter() + " id=" +  i);
	        String letter = this.trayTiles.get(i).getCurrentLetter();
	        this.trayTiles.get(i).setCurrentLetter(loopLetter);
	      //  Logger.d(TAG, "handleDropOnTray currentLetter=" +  this.trayTiles.get(i).getCurrentLetter() + " id=" +  i);
	        loopLetter = letter;

	        //stop at first formerly empty tile 
	        if (letter == "") { loopRun = false; }
	        i = i + (1 * j);
	        escapeValue += 1;
	        if (escapeValue > 7) {break;} ///just to stop a runaway freight train
	    }
	    
		this.clearCurrentTrayTile();
		this.clearDropTargetTrayTile(); 
		this.clearDraggingTile();
		this.clearTargetTile();
		this.resetPointsView();
		
		 //draggingTrayTile = null;
		return;
	   
	 }
	 
	 private void resetPointsView(){
		 if (!this.parent.getGame().isCompleted()){
			 try{
				 	//PlacedResult placedResult = GameService.checkPlayRules(parent, this.defaultLayout, this.parent.getGame(), this.tiles, this.trayTiles, true);
					PlacedResult placedResult = GameService.checkPlayRules(parent, this.defaultLayout, this.parent.getGame(), this.tiles, true);
					this.parent.setPointsView(placedResult.getTotalPoints());
				}
				catch (DesignByContractException e){
					this.parent.setPointsView(0);
				}
		 }
	 }
	 
	 
	 
	 //private GameTile findTapTargetTile(int xPosition, int yPosition){
	 private int findTapTargetTile(int xPosition, int yPosition){
		 int tileId = -1; 
		 //GameTile tapTargetTile = null;
		 boolean zoomed = this.isZoomed;
 
		 Logger.d(TAG, "findTapTargetTile yPosition=" + yPosition + " trayAreaTop=" + this.trayAreaRect.getTop());
		 
		 //let's see if a tile was tapped on 
		 if (zoomed){
			 //in zoom mode, make sure user didn't tap outside of board view, else we might get a false positive hit 
			 //on tiles that are underneath the tray
			 if (yPosition < this.trayAreaRect.getTop()){ //this.trayAreaTop){
				 tileId = this.FindTileFromPositionInZoomedMode(xPosition, yPosition);
			 }
			 else{
				 Logger.d(TAG, "findTapTargetTile yPosition is below this.trayAreaRect.getTop()");
				 
				 
			 }
			 
		 }
		 else{
			 tileId = this.FindTileFromPositionInFullViewMode(xPosition, yPosition);
		 }
		 
		 return tileId; //tapTargetTile;

	 }
	 
	 private DropTarget findClosestTrayTile(int xPosition, int yPosition){
		 //TrayTile dropTargetTile = null;
		 int tileId = -1;
		 
		 //instead of using finger location use the center of the dragged tile. this will be more accurate along the boundaries
		 //especially if the user slides finger out of bounds but the dragging tile is limited to the actual boundaries
		// Coordinate center = this.findCenterOfDraggingTile();
		 
		// int xPosition = center.getxLocation();
		// int yPosition = center.getyLocation();
		 
		 
		// Logger.d(TAG, "findClosestTrayTile isCoordinateWithinArea=" + this.trayAreaRect.isCoordinateWithinArea(xPosition, yPosition));
		// Logger.d(TAG, "findClosestTrayTile trayArea top=" + this.trayAreaRect.getTop() + " bottom=" + this.trayAreaRect.getBottom() + 
		//		" left=" + this.trayAreaRect.getLeft() + " right=" + this.trayAreaRect.getRight());
		 
		// Logger.d(TAG,  "findClosestTrayTile xPosition=" + xPosition + " yPosition=" + yPosition);
		 
		 //if x and y positions are anywhere within the entire tray area (including borders, gaps etc)
		 //then find closest based on center of tiles	
		 int smallestSummedDifference = 250000;
		 
		 //ignore this because tile might be dropped in lower padding area in full view mode
		// if (this.trayAreaRect.isCoordinateWithinArea(xPosition, yPosition)){
			
			 for(TrayTile tile : this.trayTiles){

			 
				int summedDifference = Math.abs(tile.getxPositionCenter() - xPosition) +
						 Math.abs(tile.getyPositionCenter() - (yPosition));
				 
				//Logger.d(TAG, "findClosestTrayTile xCenter=" + tile.getxPositionCenter() + " yCenter=" + tile.getyPositionCenter());

				 
				 if (summedDifference < smallestSummedDifference){
					 tileId = tile.getId(); //dropTargetTile = tile;
					 smallestSummedDifference = summedDifference;
				 }
					// Logger.d(TAG, "findClosestOpenTile xCenter=" + (zoomed ? tile.getxPositionCenterRelativeZoomed() : tile.getxPositionCenter()) + " yCenter=" +  (zoomed ? tile.getyPositionCenterRelativeZoomed() : tile.getyPositionCenter()));
			//		  Logger.d(TAG, "findClosestTrayTile summedDifference=" + summedDifference + " smallestSummedDifference=" + smallestSummedDifference);
			 
			 }
		//	 Logger.d(TAG, "findClosestTrayTile closestTile.Id=" + tileId);
		//}
	
		 return new DropTarget(tileId, smallestSummedDifference); //dropTargetTile;
	 }
	 
	 private void setClosestDropTarget(){
		 //find the closest available tile, either on board or tray.
		 //if we are in zoomed mode, make sure not to find any tiles that are fully below the tray
		
		 int xPosition = this.currentX;
		 int yPosition = this.currentY;
		 
	//	 this.parent.captureTime(TAG + " setClosestDropTarget started");
		  Logger.d(TAG, "setClosestDropTarget pre xPosition=" + xPosition + " yPosition=" + yPosition);
		 //if the finger location is outside of the visible bounds of the game surface
		 //instead of using specific finger location from onTouchEvent, let's use the center of the dragging tile 
		 if (this.visibleAreaRect.isCoordinateWithinArea(xPosition, yPosition)){
		
			 Coordinate center = this.findCenterOfDraggingTile();
			 
			 xPosition = center.getxLocation();
			 yPosition = center.getyLocation();
		 }

		  Logger.d(TAG, "setClosestDropTarget post xPosition=" + xPosition + " yPosition=" + yPosition);

		 this.parent.captureTime(TAG + " findClosestOpenTile starting");
		 //first lets check board tiles
		 DropTarget boardDrop = this.findClosestOpenTile(xPosition, yPosition);
		 
		 this.parent.captureTime(TAG + " findClosestOpenTile ended");
		  Logger.d(TAG, "boardDrop distance=" + boardDrop.getDistance() + " tileId=" + boardDrop.getTileId() + " letter=" + boardDrop.getTileId());
		 
		 this.parent.captureTime(TAG + " findClosestTrayTile starting");
		 //then lets check tray tiles
		 DropTarget trayDrop = this.findClosestTrayTile(xPosition, yPosition);

		 this.parent.captureTime(TAG + " findClosestTrayTile ended");
		 //Logger.d(TAG, "trayDrop distance=" + trayDrop.getDistance() + " tileId=" + trayDrop.getTileId());
		 
		 //now determine which was closer, the closest board tile or closest tray tile...it has to have been dropped on at least one
		 if (boardDrop.getTileId() > -1 && boardDrop.getDistance() <= trayDrop.getDistance() ){
			 this.targetTileId  = boardDrop.getTileId();
			 this.clearDropTargetTrayTile();
		 }
		 else{
			 this.dropTargetTrayTileId = trayDrop.getTileId();
			 this.clearTargetTile();
		 }
		// Logger.d(TAG, "trayDrop this.targetTileId=" + this.targetTileId + " dropTargetTrayTileId=" + this.dropTargetTrayTileId);
		// this.parent.captureTime(TAG + " setClosestDropTarget ending");
	 }
	 
	 private DropTarget findClosestOpenTile(int xPosition, int yPosition){
		 int dropTargetTileId = -1;
		 //GameTile dropTargetTile = null;
		 boolean zoomed = this.isZoomed;
		 
		 this.parent.captureTime(TAG + " findClosestOpenTile internal starting");
		 //when in zoomed mode, use the boardAreaRect to readjust the center position
		 //of each tile when checking for the the closest drop target.  this adjusts for 
		 //board tiles that are mostly hidden behind the tray for instance.  they should
		 //not be chosen incorrectly when using this technique
		 
		 //let's see if a tile was dropped on 
		 if (zoomed){
			 dropTargetTileId = this.FindTileFromPositionInZoomedMode(xPosition, yPosition);
		 }
		 else{
			 dropTargetTileId = this.FindTileFromPositionInFullViewMode(xPosition, yPosition);
		 }

		 //if the drop target was not a board tile, return nothing
		 if (dropTargetTileId == -1) {return new DropTarget(-1,0);}
		 
		 //if a tile was dropped on, is that tile droppable?
		 if (this.tiles.get(dropTargetTileId).isDroppable()) {return new DropTarget(dropTargetTileId,0);}//return dropTargetTileId;}
		 
		 //ok the tile is not droppable, which means it already contains a placed letter, so let's find the next closest tile
		 
		 //convert x and y to relative position within dropTargetTile
		 
		 //Logger.d(TAG, "findClosestOpenTile orig xPosition=" + xPosition + " orig yPosition=" + yPosition);
		 //first let's find the distance the current position is from the left and top of the tapped tile
		 int xDiff = xPosition - (zoomed ? this.tiles.get(dropTargetTileId).getxPositionZoomed() : this.tiles.get(dropTargetTileId).getxPosition()); 
		 int yDiff = yPosition - (zoomed ? this.tiles.get(dropTargetTileId).getyPositionZoomed() : this.tiles.get(dropTargetTileId).getyPosition());
	
		 //Logger.d(TAG, "findClosestOpenTile xDiff=" + xDiff + " yDiff=" + yDiff);
		 //Logger.d(TAG, "findClosestOpenTile zoomedtilewidth=" + this.zoomedTileWidth + " droppedTile row=" + dropTargetTile.getRow() + " col=" + dropTargetTile.getColumn());
		 
		 //grab the relative edges, the zoomed relative is based on center so let's subtract half the width of the zoomed tile to find the edge
		 int leftEdge = (zoomed ? (this.tiles.get(dropTargetTileId).getxPositionCenterRelativeZoomed() - (this.zoomedTileWidth / 2)) : this.tiles.get(dropTargetTileId).getxPosition());
		 int topEdge = (zoomed ? (this.tiles.get(dropTargetTileId).getyPositionCenterRelativeZoomed() - (this.zoomedTileWidth / 2)) : this.tiles.get(dropTargetTileId).getyPosition());

		 //Logger.d(TAG, "findClosestOpenTile leftEdge=" + leftEdge + " topEdge=" + topEdge);
		 
		 //now use the differences to find the revised positions based on the relative positions
		 xPosition = leftEdge + xDiff;
		 yPosition = topEdge + yDiff;
		 
		 //Logger.d(TAG, "findClosestOpenTile xPosition=" + xPosition + " yPosition=" + yPosition);
		 
		 int smallestSummedDifference = 250000;
		 
		 for(GameTile tile : this.tiles){
			 RectArea tileRect = null;
			 if (this.isZoomed){
				 tileRect = new RectArea(tile.getyPositionZoomed(), tile.getxPositionZoomed(), tile.getyPositionZoomed() + this.zoomedTileWidth, tile.getxPositionZoomed() + this.zoomedTileWidth); 
			 }
			 else{
				 tileRect = new RectArea(tile.getyPosition(), tile.getxPosition(), tile.getyPosition() + this.fullViewTileWidth, tile.getxPosition() + this.fullViewTileWidth); 			 
			 }
			 if (tile.isDroppable() && this.boardAreaRect.isRectPartiallyWithinArea(tileRect)){
				 int summedDifference = Math.abs((zoomed ? tile.getxPositionCenterRelativeZoomed() : tile.getxPositionCenter()) - xPosition) +
						 Math.abs((zoomed ? tile.getyPositionCenterRelativeZoomed() : tile.getyPositionCenter()) - (yPosition));
				 
				 //only use this tile if its inside board area
				// if (!zoomed || (zoomed && this.boardAreaRect.isRectPartiallyWithinArea(new RectArea(yPosition, xPosition, yPosition + this.zoomedTileWidth + this.tileGap, xPosition + this.zoomedTileWidth + this.tileGap)))){
					 if (summedDifference < smallestSummedDifference){
						 dropTargetTileId = tile.getId();
						 smallestSummedDifference = summedDifference;
					 }
				// }
				 
				// Logger.d(TAG, "findClosestOpenTile xCenter=" + (zoomed ? tile.getxPositionCenterRelativeZoomed() : tile.getxPositionCenter()) + " yCenter=" +  (zoomed ? tile.getyPositionCenterRelativeZoomed() : tile.getyPositionCenter()));
				// Logger.d(TAG, "findClosestOpenTile summedDifference=" + summedDifference + " smallestSummedDifference=" + smallestSummedDifference);
			 }
		 }
		 //Logger.d(TAG, "findClosestOpenTile closestTile.Id=" + dropTargetTile.getId());
		 return new DropTarget(dropTargetTileId,smallestSummedDifference);
	 }
	 
	 //Lint made me do it
	 @SuppressLint("WrongCall")
	protected void drawFromThread(Canvas canvas) {
		 onDraw(canvas);
	 }
	 
	 @Override
	 protected void onDraw(Canvas canvas) {
		// this.parent.captureTime("onDraw starting");
		 if (canvas == null){
			 Logger.d("TAG", "onDraw canvas is null");
			 return;
		 }
		 
		 this.parent.captureTime(TAG + " onDraw started");
		 
 		// super.onDraw(canvas);
	// Logger.d(TAG,  "onDraw motion=" + this.currentTouchMotion + " " + " zoom=" + this.isZoomed + " tapCheck=" + this.tapCheck + " osMoving=" +  this.isMoving  + " readyToDraw=" + this.readyToDraw + " prevX=" + this.previousX + " prevY=" + this.previousY
	// 			 + " currX=" + this.currentX + " currY=" + this.currentY);
	 
	// Logger.d(TAG, "this.getDraggingTile()=" + (this.getDraggingTile() == null));
		 
	//	long currentTouchTime = System.nanoTime();
		
		if (!this.shuffleRedraw && !this.recallLettersRedraw  && !this.afterPlayRedraw && !this.trayTileTapped && !wordHintRedraw){
			//  if (this.touchMotion == MotionEvent.ACTION_MOVE ) {this.readyToDraw = false;} 
			 if (this.currentTouchMotion == MotionEvent.ACTION_DOWN && 
					 (this.getCurrentTrayTile() != null && !this.getCurrentTrayTile().isDragging())){ 
			//	 Logger.d(TAG, "currentTrayTile not dragging");
				 this.readyToDraw = false;}  
			 
			//this will have to change if dragging a tile 
			 //if(this.currentTrayTile == null || !this.currentTrayTile.isDragging()){
			 
			 //only drag in full view mode if dragging a tile, since board will only drag in zoomed view mode
			 if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && this.isZoomed == false) {
				 if (this.getDraggingTile() != null) {}
				 else if (this.getCurrentTrayTile() == null || !this.getCurrentTrayTile().isDragging()) { 
			//		 Logger.d(TAG, "getCurrentTrayTile nullish");
					 this.readyToDraw = false; 
				 }
			 }
			 
			// if ((this.getCurrentTrayTile() == null || !this.getCurrentTrayTile().isDragging() && 
			// 	(this.getDraggingTile() == null || !this.getDraggingTile().isDragging() || !this.getDraggingTile().isDragPending())){
			//	 if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && this.isZoomed == false) { this.readyToDraw = false; }
			// }
			 //if we are in middle of action move but we are not moving (the finger is pressed but not moving, don't redraw
			 //this condition was causing major shaking issues on the device, commented out for now
	 		 if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && 
	 			this.previousTouchMotion == MotionEvent.ACTION_MOVE &&
	 			this.currentX <= Math.round(this.previousX * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
	 			this.currentX >= Math.round(this.previousX * (1 - MOVEMENT_TRIGGER_THRESHOLD)) && 
	 			this.currentY <= Math.round(this.previousY * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
	 			this.currentY >= Math.round(this.previousY * (1 - MOVEMENT_TRIGGER_THRESHOLD))){
	 		//	 Logger.w(TAG,"onDraw minimum threshold not met");
	 			// 	this.readyToDraw = false; 
	 			 }
			}
		
	
		// Logger.d(TAG, "onDraw this.readyToDraw=" + this.readyToDraw);
		 if (this.readyToDraw == true){ 
			
			 // Logger.w(getClass().getSimpleName() + "onDraw2 ",this.currentTouchMotion + " " + this.tapCheck + " " +  this.isMoving  + " " + this.readyToDraw + " " + this.previousX + " " + this.previousY
			//	 		 + " " + this.currentX + " " + this.currentY + " " + this.previousTouchMotion);
			 
			// if (this.touchMotion != MotionEvent.ACTION_MOVE ) { 
			// canvas.drawColor(0, Mode.CLEAR); ///clears out the previous drawing on the canvas
		 
			 //}
			 this.readyToDraw = false; 
			 
			 if (this.isZoomed == false || this.isZoomAllowed == false){
				 
				 this.drawFullBoard(canvas);
				
			//	 Logger.d(TAG, "onDraw this.isZoomed == false");
				 this.readyToDraw = false;
			 }
			 else {
				// if (this.touchMotion == MotionEvent.ACTION_UP) {
				//	 this.isZoomed = false; ///turn off zoom since we are handling now
				// }
				 
			   Logger.d(TAG, "onDraw this.shuffleRedraw=" + this.shuffleRedraw);
			   Logger.d(TAG, "onDraw this.trayTiles.get(0).getxPosition()=" + this.trayTiles.get(0).getxPosition() + " " + this.trayTiles.get(0).getxPositionDragging());
				 //a tray tile has been tapped down
				 if (this.currentTouchMotion == MotionEvent.ACTION_DOWN && 
						 ((this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()) || this.isBoardTileDragging())){
					 //board stays the same
					 this.drawBoardOnMove(canvas, 0, 0);
					 Logger.d(TAG, "onDraw a tray tile has been tapped down");
				 }
				 else if (this.shuffleRedraw || this.recallLettersRedraw || this.afterPlayRedraw || this.trayTileTapped || this.wordHintRedraw){
					 //these actions just mean the board should be redrawn as is
					 this.drawBoardOnMove(canvas, 0, 0);
				//	 this.shuffleRedraw = false;
				//	 this.recallLettersRedraw = false;
				//	 this.afterPlayRedraw = false;
				//	 this.trayTileTapped = false;
				 }
				 else if (this.isMomentum){
					 Logger.w(TAG,"onDraw drawMomentumScroll about to be called");
					 this.drawMomentumScroll(canvas);
				 }
				 else if(this.currentTouchMotion == MotionEvent.ACTION_MOVE &&
						 this.isTrayTileDragging() || this.isBoardTileDragging()  ){
					 //do not move the board, a tile is being dragged 
					 this.drawBoardOnMove(canvas, 0, 0);
				 }
				 //only draw board moving if a tile is not moving (which was checked above)
				 else if (this.currentTouchMotion == MotionEvent.ACTION_MOVE) {
				//	 Logger.w(TAG,"onDraw drawBoardOnMove about to be called");
					 this.drawBoardOnMove(canvas, this.previousX - this.currentX, this.previousY - this.currentY);
				
				 }
				 else if (this.currentTouchMotion == MotionEvent.ACTION_UP){ 	
					
					 this.parent.captureTime(TAG + " onDraw action_up started");
				// 	this.parent.updateHandler.sendMessage(this.parent.updateHandler.obtainMessage(GameSurface.MSG_SCOREBOARD_VISIBILITY, GONE, 0));
					 
			//		MarginLayoutParams  params = (MarginLayoutParams )this.getLayoutParams();
			//		params.setMargins(params.leftMargin, params.topMargin - 50, params.rightMargin, params.bottomMargin); //substitute parameters for left, top, right, bottom
			//		this.setLayoutParams(params); 
					canvas.drawColor(0, Mode.CLEAR);
					

					//if board is already zoomed and a tile was dropped, do not move board, keep it in the same position
					if ((this.trayTileDropped || this.trayTileDropTarget) && this.alreadyInZoomedState){
						
						 this.parent.captureTime(TAG + " drawBoardOnMove 1 started");
						this.drawBoardOnMove(canvas, 0, 0); //////////////?????????????
					}
					else if (this.getTargetTile() != null){
						
						this.parent.captureTime(TAG + " drawBoardZoomOnUp 1 started");
						this.drawBoardZoomOnUp(canvas);
					}
					else{
						//assume that if the current tile is null, just keep board as is
						this.parent.captureTime(TAG + " drawBoardOnMove 2 started");
					 
						this.drawBoardOnMove(canvas, 0, 0);
					    
					}
					this.readyToDraw = false;
					this.parent.captureTime(TAG + " onDraw action_up ended");
				  
				 }
			 }
			// this.previousTouchMotion = this.currentTouchMotion;
			// this.currentTouchMotion = ;
			 
			// Logger.d(TAG, "onDraw about to draw tray");
		    this.drawTray(canvas);
		    
		    this.drawDraggingTile(canvas);
		   
		    //on a drop event (action_up) check to determine if shuffle and recall buttons need to be switched
		    if (this.currentTouchMotion == MotionEvent.ACTION_UP || this.recallLettersRedraw || this.wordHintRedraw){
		    	Logger.d(TAG, "onDraw MotionEvent.ACTION_UP about to call this.setButtonStates");
		    	this.setButtonStates();
		    }
		    
		    if (this.shuffleRedraw || this.recallLettersRedraw || this.afterPlayRedraw || this.trayTileTapped || this.wordHintRedraw){
		    	this.shuffleRedraw = false;
		    	this.recallLettersRedraw = false;
		    	this.afterPlayRedraw = false;
		    	this.trayTileTapped = false;
		    	this.readyToDraw = false;
		    	this.wordHintRedraw = false;
		    	
		    }
		    
		   
		 } 
		 this.onDrawCounter += 1;
		 if (this.onDrawCounter == 1){
			 this.parent.onInitialRenderComplete();
		 }
		
		 this.parent.captureTime("onDraw ended");
	 }
   
	 private void drawDraggingTile(Canvas canvas){
		 Coordinate position;
		 
		 //let's see if a board tile is being dragged
		 //(dragging a board tile means the player has previously dropped a tray tile on the board and
		 //now he is moving it again
		 if (this.getDraggingTile() != null){
			 position = this.drawDraggingTileGuts(canvas, this.getDraggingTile().getDraggingLetter());
			 this.getDraggingTile().setxPositionDragging(position.getxLocation());
			 this.getDraggingTile().setyPositionDragging(position.getyLocation());
			 
				
			// Logger.d(TAG, "drawDraggingTile positionX=" + position.getxLocation() + " Y=" + position.getyLocation());

		 }
		 //let's see if a tray tile is being dragged
		 else if (this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()){
			 position = this.drawDraggingTileGuts(canvas, this.getCurrentTrayTile().getDraggingLetter());
			
			// Logger.d(TAG, "drawDraggingTile positionX=" + position.getxLocation() + " Y=" + position.getyLocation());
			 
			 this.getCurrentTrayTile().setxPositionDragging(position.getxLocation());
			 this.getCurrentTrayTile().setyPositionDragging(position.getyLocation());
		 }
		 
	 }
	 
	 private Coordinate findCenterOfDraggingTile(){
		 Coordinate center = new Coordinate();
		 if (this.getDraggingTile() != null && this.getDraggingTile().isDragPositionSet()){
			 
		//	 Logger.d(TAG,"findCenterOfDraggingTile x=" + this.getDraggingTile().getxPositionDragging() + " y=" + this.getDraggingTile().getyPositionDragging());
			 center.setxLocation(this.getDraggingTile().getxPositionDragging() + (this.draggingTileSize / 2));
			 center.setyLocation(this.getDraggingTile().getyPositionDragging() + (this.draggingTileSize / 2));
		 }
		 //let's see if a tray tile is being dragged
		 else if (this.getCurrentTrayTile() != null && this.getCurrentTrayTile().isDragging()){
			 center.setxLocation(this.getCurrentTrayTile().getxPositionDragging() + (this.draggingTileSize / 2));
			 center.setyLocation(this.getCurrentTrayTile().getyPositionDragging() + (this.draggingTileSize / 2));
		 }
		 
		 return center;
	 }
	 
	 
	 private Coordinate drawDraggingTileGuts(Canvas canvas, String letter){
		 Coordinate position = new Coordinate();
		 
		// Logger.d(TAG, "drawDraggingTileGuts xPosition="  + position.getxLocation() + " yPosition=" + position.getyLocation());
		 
		 int xPosition = this.currentX - (this.draggingTileSize / 2);
		 int yPosition = this.currentY - (this.draggingTileSize / 2);
		 
	//	 Logger.d(TAG, "drawDraggingTileGuts xPosition="  + xPosition + " yPosition=" + yPosition);
		 
		 //adjust if over any of the edges 
		 if (xPosition < this.visibleAreaRect.getLeft() - Math.round(this.draggingTileSize / 4)){
			 //we are outside of the left edge
			 xPosition = this.visibleAreaRect.getLeft() - Math.round(this.draggingTileSize / 4);
		 }
		 if (xPosition + this.draggingTileSize > this.visibleAreaRect.getRight() + Math.round(this.draggingTileSize / 4)){
			 //we are outside of the right edge
			 //xPosition = xPosition - (xPosition + this.draggingTileSize - this.fullWidth);
			 xPosition = this.visibleAreaRect.getRight() + Math.round(this.draggingTileSize / 4) - this.draggingTileSize;
		 }
		 if (yPosition < this.visibleAreaRect.getTop() - Math.round(this.draggingTileSize / 4)){
			 //we are outside of the top edge (might have to be adjusted for zoomed vs not zoomed
			 yPosition = this.visibleAreaRect.getTop() - Math.round(this.draggingTileSize / 4);
		 }
		 if (yPosition + this.draggingTileSize > this.visibleAreaRect.getBottom() + Math.round(this.draggingTileSize / 4)){
			 //we are outside of the bottom edge
		//	 Logger.d(TAG, "drawDraggingTile yPosition=" + yPosition + " height=" + height + " draggingTileSize=" + draggingTileSize);
			 //yPosition = yPosition - (yPosition + this.draggingTileSize - this.height);
			 yPosition =  this.visibleAreaRect.getBottom() + Math.round(this.draggingTileSize / 4) - this.draggingTileSize;
			 
		//	 Logger.d(TAG, "drawDraggingTile yPosition=" + yPosition + " Height=" + height + " draggingTileSize=" + draggingTileSize);
		 }
		 
		 position.setxLocation(xPosition);
		 position.setyLocation(yPosition);
		 
		// Logger.d(TAG, "drawDraggingTileGuts xPosition_="  + position.getxLocation() + " yPosition_=" + position.getyLocation());
		 
		 canvas.drawBitmap(GameSurfaceView.bgTrayBaseDragging, xPosition, yPosition, null);

	 
     	 Paint pLetter = new Paint();
     	 pLetter.setColor(Color.parseColor(this.parent.getString(R.color.game_board_dragging_tile_letter)));
     	 pLetter.setTextSize(Math.round(this.draggingTileSize * .78));
     	 pLetter.setAntiAlias(true); 
     	 pLetter.setTypeface(ApplicationContext.getLetterTypeface()); //(this.letterTypeface);
	     Rect boundsLetter = new Rect();
	     Rect boundsLetterHeight = new Rect();
	     
	     //always base vertical dimension on single letter (T).  based on the font, letters of different height were screwing up the even look
	     pLetter.getTextBounds("T", 0, 1, boundsLetterHeight);
	     pLetter.getTextBounds(letter, 0, letter.length(), boundsLetter);
	     
	     //find the midpoint and scoot over 5% to the left and 5% down
	     int textLeft = xPosition + this.draggingTileMidpoint - Math.round(this.draggingTileMidpoint * .15f) - (Math.round(boundsLetter.width() / 2));
	     int textTop =  yPosition + this.draggingTileMidpoint + Math.round(this.draggingTileMidpoint * .08f) + (Math.round(boundsLetterHeight.height() / 2));
	     
	     canvas.drawText(letter, textLeft, textTop, pLetter);
	     
	     Paint pValue = new Paint();
	     pValue.setColor(Color.parseColor(this.parent.getString(R.color.game_board_dragging_tile_value)));
	     pValue.setTextSize(Math.round(this.draggingTileSize * .20f));
	     pValue.setAntiAlias(true);
	     
	     pValue.setTypeface(Typeface.SANS_SERIF);
	     Rect boundsValue = new Rect();
	     String value = Integer.toString(AlphabetService.getLetterValue(letter));
	     pValue.getTextBounds(value, 0, value.length(), boundsValue);
	     
	     //find the midpoint and scoot over 5% to the left and 5% down
	     int textLeftValue =  xPosition + this.draggingTileSize - boundsValue.width() - Math.round(this.draggingTileSize * .12f);
	     int textTopValue =  yPosition + Math.round(this.draggingTileSize * .15f) + (Math.round(boundsValue.height() / 2));
	     
	     canvas.drawText(value, textLeftValue, textTopValue, pValue);
	     
	     return position;
	 }
	 
	 
	private void drawFullBoard(Canvas canvas){
	//	Logger.d(TAG, "drawFullBoard");
		 canvas.drawColor(0, Mode.CLEAR);
		 this.drawUpperGap(canvas);
		 this.drawFullView(canvas);
		 this.drawLowerGap(canvas);	
	}
	
	private void drawBoardOnMove(Canvas canvas, int leftDiff, int topDiff){
	///	Logger.d(TAG, "drawBoardOnMove leftDiff=" + leftDiff + " topDiff=" + topDiff);
		
	//	Logger.d(TAG, "drawBoardOnMove this.trayTiles.get(0).getxPosition()=" + this.trayTiles.get(0).getxPosition() + " " + this.trayTiles.get(0).getxPositionDragging());
		 this.readyToDraw = false;
		 this.parent.captureTime(TAG + " drawBoardOnMove started");
		 boolean setReadyToDraw = false;
	//	 int leftDiff = this.previousX - this.currentX ;
	//	 int topDiff =  this.previousY - this.currentY;
		 
		 //handle tile drag later, first drag the whole board around
		// if (this.currentX < this.outerZoomLeft || this.currentY < this.outerZoomTop) {
		//	 this.readyToDraw = false; 
		//	 newLeft = 
		// }
		 
		// Logger.w(TAG,"drawBoardOnMove: leftDiff=" + leftDiff + " topDiff=" + topDiff + " prevX=" +  this.previousX  + " prevY=" + this.previousY + 
		//		 " currX="  +  this.currentX  + " currY=" + this.currentY + " outerZoomLeft=" 
		//		 + this.outerZoomLeft + " outerZoomTop=" + this.outerZoomTop);
		 
		  this.previousX = this.currentX;
		  this.previousY = this.currentY;
	 
		 
						
//		 if (this.targetTile.getPlacedText().length() > 0 ){
//			 //drag this letter, not the board
//		 }
//		 else {
			//drag/scroll entire board 
		 
		 
			 //if (tappedTile == null) { return; } ///do something here, this is causing the board to disappear when scrolled out of bounds
			 
			 //grab top left tile  
			 GameTile topLeftTile = this.tiles.get(0);
			 
			// Logger.w(getClass().getSimpleName() + "onDraw ACTION_MOVE ",leftDiff + " " + topDiff + " " +  this.previousX  + " " + this.previousY + " "
			// + topLeftTile.getxPositionZoomed() + " " + topLeftTile.getyPositionZoomed() + " "
			// + this.outerZoomLeft + " " + this.outerZoomTop);

			 this.parent.captureTime(TAG + " drawBoardOnMove check bounds starting ");
			 
		//	 Logger.w(TAG, "drawBoardOnMove: topLeftTile.getxPositionZoomed()=" + topLeftTile.getxPositionZoomed() + " topLeftTile.getyPositionZoomed()=" + topLeftTile.getyPositionZoomed());
		//	 Logger.w(TAG, "drawBoardOnMove: this.outerZoomLeft=" + this.outerZoomLeft + " this.outerZoomTop=" + this.outerZoomTop);

			 //make sure it will be within outer left bounds
			 if (topLeftTile.getxPositionZoomed() - leftDiff < this.outerZoomLeft){
				 //only scroll to the edge of the left outer boundary
				 //leftDiff = leftDiff - (this.outerZoomLeft - topLeftTile.getxPositionZoomed() - leftDiff);
				 //leftDiff = this.outerZoomLeft - topLeftTile.getxPositionZoomed(); //topLeftTile.getxPositionZoomed() + this.outerZoomLeft; //leftDiff - (this.outerZoomLeft - topLeftTile.getxPositionZoomed());  
				 leftDiff = topLeftTile.getxPositionZoomed() - this.outerZoomLeft; //topLeftTile.getxPositionZoomed() + this.outerZoomLeft; //leftDiff - (this.outerZoomLeft - topLeftTile.getxPositionZoomed());  
					
				 
		//		 Logger.w(TAG, "drawBoardOnMove: leftdiff(1)=" + leftDiff);
			 } 
			 else {
				 //make sure it will be within visible left bounds
				 if (topLeftTile.getxPositionZoomed() - leftDiff > 0) {
					 leftDiff = topLeftTile.getxPositionZoomed() - 0;//leftDiff - (1 - topLeftTile.getxPositionZoomed() - leftDiff);   
					 
			//		 Logger.w(TAG, "drawBoardOnMove: leftdiff(2)=" + leftDiff);
				 }
				 else {
					 setReadyToDraw = true;
				 }
			 }
			 
			//grab top left tile and make sure it will be within outer top bounds
			 if (topLeftTile.getyPositionZoomed() - topDiff < this.outerZoomTop){ 
				 //only scroll to the edge of the top outer boundary
				 topDiff = this.outerZoomTop;// - topLeftTile.getyPositionZoomed();//topLeftTile.getyPositionZoomed() + this.outerZoomTop; //topDiff - (this.outerZoomTop - topLeftTile.getyPositionZoomed() - topDiff);
				 //topDiff = topDiff - (this.outerZoomTop - topLeftTile.getyPositionZoomed());
				 topDiff = topLeftTile.getyPositionZoomed() - this.outerZoomTop;
		//		 Logger.w(TAG, "drawBoardOnMove: topdiff(1)=" + topDiff);
			 }
			 else { 
				 //make sure it will be within visible top bounds
				 if (topLeftTile.getyPositionZoomed() - topDiff > 0) {
					 topDiff = topLeftTile.getyPositionZoomed();// - 1;//topDiff - (1 - topLeftTile.getyPositionZoomed() - topDiff);
					 
			//		 Logger.w(TAG, "drawBoardOnMove: topdiff(2)=" + topDiff);
				 }
				 else {
					 setReadyToDraw = true;
				 }
			 }

			 this.parent.captureTime(TAG + " drawBoardOnMove check bounds ended ");
				// this.previousX = this.currentX;
				// this.previousY = this.currentY;
				 canvas.drawColor(0, Mode.CLEAR);
				 
				 this.parent.captureTime(TAG + " drawZoomedBoardByDiff  starting ");
				 this.drawZoomedBoardByDiff(canvas, leftDiff, topDiff);	
				 
				 this.parent.captureTime(TAG + " drawZoomedBoardByDiff  ended ");
				 
				 if (setReadyToDraw){ this.readyToDraw = true; }
		  //   this.loadZoomedBoardByDiff(canvas, leftDiff, topDiff);	
		   //  if (setReadyToDraw){this.readyToDraw = true;}
//		}
		
	} 
	
	
	private void drawMomentumScroll(Canvas canvas){
	            // Free scrolling. Decelerate gradually.
		//grab velocity from speed of movement before action_up

		
		
		//for smooth scrolling you'd need to make some sort of method that takes a few points after scrolling
		//(i.e the first scroll point and the 10th) , subtract those and scroll by that number in a for each loop that makes it gradually slower
		//( ScrollAmount - turns - Friction ).
		//You could simulate this with a "recent axis changes" queue.

		//If you store say the last half a second of changes with the corresponding timestamps, you can then 
		//test if the queue is longer than a value N (ie if the user dragged it quicker than usual towards the end). 
		//You know the total distance traveled in the last half a second, the time, from those you can get a speed.

		//Scale the speed to something reasonable (say.. for 15px/.5sec, map to ~25px/sec) and apply a negative acceleration
		//(also appropiately scaled, for the example above, say -20px/sec) every couple of milliseconds 
		//(or as fast as your system can easily handle it, don't overstress it with this).

		//Then run a timer, updating the speed at each tick (speed+=accel*time_scale), then the position (position+=speed*time_scale). When the speed reaches 0 (or goes below it) kill the timer.

		try {
			Thread.sleep(this.momentumScrollInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 //this.momentumScrollInterval = Math.round(this.momentumScrollInterval * 1.25f);
		
		this.xDistance -= (this.xDistance > 0 ? +1 : -1) * .02;
		this.yDistance -= (this.yDistance > 0 ? +1 : -1) * .02;
		
		
		 this.drawBoardOnMove(canvas, this.xDistance, this.yDistance);
	        
	        if (this.xDistance == 0 && this.yDistance == 0){
	        	this.readyToDraw = false;
	        }
	        
	        //Logger.w(TAG, "drawMomentumScroll xDistance=" + this.xDistance + " yDistance=" + this.yDistance );
	        
	        if (!this.readyToDraw){this.isMomentum = false;}
		
	}
	
	
	 
	private void drawBoardZoomOnUp(Canvas canvas){//, GameTile focusedTile){
		// TrayTile tappedTrayTile = null;
		 //GameTile tappedTile = this.targetTile; //this.FindTileFromPositionInFullViewMode(this.currentX, this.currentY);    
		 

		 
		 //we are coming out of a single click here.  this means the player tapped on any area outside of the board.  lets just redraw the board
		 //but also clear out the current tray tile just in case it was tapped
		 //if (tappedTile == null){
		//	 Logger.d(TAG, "drawBoardZoomOnUp tapped tile is null");
		//	 //clear current tray tile
		//	 this.currentTrayTile = null; //this.FindTrayTileFromPositionInFullViewMode(this.currentX, this.currentY);
		// 
		//	 //redraw the board as is  
		//	 this.drawBoardOnMove(canvas, 0, 0);
		// }
		 //else{
		 
		     //find the equivalent tapped top location in zoomed layout
			 int tappedTop = this.midpoint - (((this.getTargetTile().getRow() - 1) * this.zoomedTileWidth) + Math.round(this.zoomedTileWidth / 2));
			 
			 //make sure we don't pass the upper top boundary (this upper boundary is calculated to ensure that bottom of board does
			 //not render too high)
			 if (tappedTop < this.outerZoomTop) {tappedTop = this.outerZoomTop;}
			 
			 //make sure we don't pass the visible top boundary (this is the visible top boundary of the surface view minus padding)
			 if (tappedTop > 0) {tappedTop = 0;}
			 
			 //find the equivalent tapped left location in zoomed layout
			 int tappedLeft = this.midpoint - (((this.getTargetTile().getColumn() - 1) * this.zoomedTileWidth) + Math.round(this.zoomedTileWidth / 2));
			 
			//make sure we don't pass the far left boundary (this far left boundary is calculated to ensure that right side of the board does
			 //not render too far to the left)
			 if (tappedLeft < this.outerZoomLeft) {tappedLeft = this.outerZoomLeft;}
			 
			 //make sure we don't pass the visible left boundary (this is the visible left boundary of the surface view minus padding)
			 if (tappedLeft > 1) {tappedLeft = 1;}
			 
			 //draw the board to the canvas
			 this.drawZoomedBoard(canvas, tappedLeft, tappedTop); 
			  
			 //release the current tile context 
			 //this.targetTile = null;  
			 this.clearTargetTile();
		// }
   }
	
	private void drawZoomedBoardByDiff(Canvas canvas, int leftDiff, int topDiff) {
		Logger.d(TAG, "drawZoomedBoardByDiff leftDiff=" + leftDiff + " topDiff=" + topDiff);	
		
		//check for odd condition of zoom positions not being set
		//assume if both the first and second tiles have not position, then none of them do
		//not 100% sure what causes this condition but I think it happens when there is a placed tile on board
		//when the game is loaded and then that tile is dragged.  the targetTileId is not set and boom, things do awry
		//this logic will set the target tile to the middle tile on the board.  the zoom might look a little out of position
		//but at least it won't dray every tile on top of each other in the top left position, as it was doing before this fix
		if (this.tiles.get(0).getxPositionZoomed() == 0 && this.tiles.get(0).getyPositionZoomed() == 0 &&
			this.tiles.get(1).getxPositionZoomed() == 0 && this.tiles.get(1).getyPositionZoomed() == 0) {
			
			//fix zoom position (use middle tile as basis) 
			this.targetTileId = 112;
			this.drawBoardZoomOnUp(canvas);
			return;
		}
		
	    for (GameTile tile : this.tiles) {
	    	tile.setxPositionZoomed(tile.getxPositionZoomed() - leftDiff);
	     	 tile.setyPositionZoomed(tile.getyPositionZoomed() - topDiff);
	     	 
	 //    	 if (tile.getxPositionZoomed() == 0 && tile.getyPositionZoomed() == 0){
	  //   		Logger.d(TAG, "drawZoomedBoardByDiff positions=0 tileId=" + tile.getId());
	  //   	 }
	 		 
	 		 this.drawZoomedBoardGuts(canvas, tile);
 
	     }
	}
	 
	private void drawZoomedBoard(Canvas canvas, int leftBasisPoint, int topBasisPoint) {
		Logger.d(TAG, "drawZoomedBoard leftBasisPoint=" + leftBasisPoint + " topBasisPoint" + topBasisPoint);
	     for (GameTile tile : this.tiles) {
	     	 tile.setxPositionZoomed(leftBasisPoint + ((tile.getColumn() - 1) * this.zoomedTileWidth) + ((tile.getColumn() - 1) * this.tileGap));
	 		 tile.setyPositionZoomed(topBasisPoint + ((tile.getRow() - 1) * this.zoomedTileWidth) + ((tile.getRow() - 1) * this.tileGap));
	 		 
	 		// tile.setxPositionCenterZoomed(tile.getxPositionZoomed() + (this.zoomedTileWidth / 2));
			// tile.setyPositionCenterZoomed(tile.getyPositionZoomed() + (this.zoomedTileWidth / 2));
	 		 
	 		 this.drawZoomedBoardGuts(canvas, tile);
	     }
	}
	
	private void drawZoomedBoardGuts(Canvas canvas, GameTile tile){
	 	// canvas.drawBitmap(tile.getOriginalBitmapZoomed(),tile.getxPositionZoomed(), tile.getyPositionZoomed(), null);
     //	Logger.d(TAG, "drawZoomedBoardGuts tile.getDisplayLetter()=" + tile.getDisplayLetter());
		// if (tile.getDisplayLetter().length() > 0){

		//DO NOT DRAW THE TILE IF IT IS FULLY OFF VIEWABLE BOARD SURFACE
		//are all 4 corners outside the visible area
		if (!this.visibleAreaRect.isCoordinateWithinArea(tile.getxPositionZoomed(), tile.getyPositionZoomed()) && //top left corner
		    !this.visibleAreaRect.isCoordinateWithinArea(tile.getxPositionZoomed() + this.zoomedTileWidth, tile.getyPositionZoomed()) && //top right corner
		    !this.visibleAreaRect.isCoordinateWithinArea(tile.getxPositionZoomed(), tile.getyPositionZoomed() + this.zoomedTileWidth) && //bottom left corner
		    !this.visibleAreaRect.isCoordinateWithinArea(tile.getxPositionZoomed() + this.zoomedTileWidth, tile.getyPositionZoomed()  + this.zoomedTileWidth) ) { //bottom right corner
			Logger.d(TAG, "drawZoomedBoardGuts outside of view tile.position=" + tile.getId());												
			return;
		
		}
		
		if (tile.getPlacedLetter().length() > 0){
    		 canvas.drawBitmap(GameSurfaceView.bgPlacedTileZoomed,tile.getxPositionZoomed(), tile.getyPositionZoomed(), null);
     
    		
    		 
    		 this.drawLetter(canvas, tile.getPlacedLetter(), this.zoomedTileWidth, tile.getxPositionZoomed(), tile.getyPositionZoomed(), false);
    		 //Logger.d(TAG, "drawZoomedBoardGuts tile.getDisplayLetter()=" + tile.getDisplayLetter());
    	 } 
		else if (tile.getOriginalLetter().length() > 0){
   		 canvas.drawBitmap(tile.isLastPlayed() ? GameSurfaceView.bgLastPlayedTileZoomed : GameSurfaceView.bgPlayedTileZoomed,tile.getxPositionZoomed(), tile.getyPositionZoomed(), null);
   	     
   		 this.drawLetter(canvas, tile.getOriginalLetter(), this.zoomedTileWidth, tile.getxPositionZoomed(), tile.getyPositionZoomed(), tile.isLastPlayed());
   		 //Logger.d(TAG, "drawZoomedBoardGuts tile.getDisplayLetter()=" + tile.getDisplayLetter());
   	 	}
	 	 //original text represents bonus text
    	 else if (tile.getOriginalText().length() > 0){
    		 
    		 
    		 canvas.drawBitmap(tile.getOriginalBitmapZoomed(),tile.getxPositionZoomed(), tile.getyPositionZoomed(), null);
    		 
    		 this.drawBonusText(canvas, tile.getOriginalText(), this.zoomedTileWidth, tile.getxPositionZoomed(), tile.getyPositionZoomed());
    		 
    	 }
    	 else {
    		 
    		 canvas.drawBitmap(tile.getOriginalBitmapZoomed(),tile.getxPositionZoomed(), tile.getyPositionZoomed(), null);
    	 }
		
	//	 if(tile.getId() == 0 || tile.getId() == 224) {
			 
	//		 Logger.d(TAG, "drawZoomedBoardGuts tile.id=" + tile.getId() + " tile.getxPositionZoomed()=" +  tile.getxPositionZoomed() + " tile.getyPositionZoomed()=" + tile.getyPositionZoomed());
	// }
	 	 
     //	 if (tile.getCurrentLetter().length() > 0){
	 //    	 Paint p = new Paint();
	 //    	 p.setColor(Color.WHITE);
	 //    	 p.setTextSize(Math.round(this.zoomedTileWidth * .6));
		//     p.setAntiAlias(true);
//		     p.setTypeface(this.bonusTypeface);
//		     Rect bounds = new Rect();
//		     p.getTextBounds(tile.getCurrentLetter(), 0, tile.getCurrentLetter().length(), bounds);
//		     int textLeft =  tile.getxPositionZoomed() + this.zoomedTileMidpoint - (Math.round(bounds.width() / 2));
//		     int textTop =  tile.getyPositionZoomed() + this.zoomedTileMidpoint + (Math.round(bounds.height() / 2));
//		     
//		     canvas.drawText(tile.getCurrentLetter(), textLeft, textTop, p);
 //    	 }
	}
		
//	}
	 
	private void drawUpperGap(Canvas canvas){
		//3366dd
		
		Paint pGap = new Paint(); 
		pGap.setColor(Color.parseColor(this.parent.getString(R.color.game_board_full_view_upper_gap_bg)));  
	    
		pGap.setAntiAlias(true);
	  //   p.setTypeface(this.bonusTypeface);
	     Rect boundsGap = new Rect();
	     boundsGap.left = 0;
	     boundsGap.right = this.fullWidth;
	     boundsGap.top = 0;
	     boundsGap.bottom = this.topGapHeight;
		
	     canvas.drawRect(boundsGap, pGap);
	     
     	 //p.setTextAlign(Paint.Align.LEFT);
     	 
     	// long textSize = Math.round(this.topGapHeight * .4);
     //	 if (textSize > MAX_TEXT_HEIGHT){
     //		 textSize = MAX_TEXT_HEIGHT;
     //	 }
     	
	 
	 	
     	
     	//"jimmy mac scored 42 points with supermans cape and batmans automobile"; 
     	String lastActionText = this.parent.getGame().getLastActionText(this.parent);
     	
        String firstLine = "";
        if (this.parent.getGame().isCompleted() ||  this.parent.getLastPlayerActionBeforeAutoplay().length() == 0){
        	firstLine = lastActionText;
        }
        else{
        	firstLine = this.parent.getLastPlayerActionBeforeAutoplay();
        }
        
     	 //determine text size and store in var
     	 long textSize = Math.round(this.topGapHeight * .4);
     	 int maxTextHeight = this.parent.getResources().getInteger(R.integer.maxGameBoardTextHeight);
     	 if (textSize > maxTextHeight){
     		 textSize = maxTextHeight;
     	 }
	  //   Paint pSizer = new Paint();

//	     pSizer.setTextSize(textSize);
//	     pSizer.setAntiAlias(true);
//	     pSizer.setTypeface(this.bonusTypeface);
//	     Rect boundsSizer = new Rect();
//	     pSizer.getTextBounds(lastActionText, 0, lastActionText.length(), boundsSizer);
	    
	     
	     int maxTextWidth = Math.round(this.fullWidth * MAX_TEXT_WIDTH);

	     Paint p = new Paint();
     	 p.setColor(Color.parseColor(this.parent.getString(R.color.game_board_full_view_upper_gap_text)));

     	 p.setTextSize(textSize);
	     p.setAntiAlias(true);
	     p.setTypeface(ApplicationContext.getBonusTypeface()); //(this.bonusTypeface);
	     Rect bounds = new Rect();
	 
	     p.getTextBounds(firstLine, 0, firstLine.length(), bounds);
	     
	     //determine if the text expands past the full width, if so just shrink the height more
	     //later figure out the logic for line breaks when line is too long
	    // Logger.d(TAG, "drawUpperGap bounds.width=" + bounds.width() + " maxTextWidth="+ maxTextWidth + " textSize=" + textSize);
	     if (bounds.width() > maxTextWidth){
	    	 double  textRatio = (double)maxTextWidth / (double)bounds.width();
		     textSize = Math.round(textSize * textRatio);
		     p.setTextSize(textSize);
		     p.getTextBounds(firstLine, 0, firstLine.length(), bounds);
		     
		  //   Logger.d(TAG, "drawUpperGap bounds.width=" + bounds.width() + " maxTextWidth="+ maxTextWidth + " textSize=" + " textRatio=" + textRatio);
	     }
	     
     	
     	 //if game is over, write 2 lines (or if there are 2 action texts)
     	 //1. the last action text
     	 //2. the random vowels and consonants used in the game
	     int textLeft =  this.midpoint - (Math.round(bounds.width() / 2));
	     int textVerticalMidpoint =  Math.round(this.topGapHeight / 2);
	     
	     //this is a hack because for some reason the vertical origin is going up in direction as opposed to down
	     int textTop = textVerticalMidpoint + (bounds.height() / 3);
	     
	  //   Logger.d(TAG, "drawUpperGap this.parent.getGame().isCompleted()=" + this.parent.getGame().isCompleted() );
	     
	     if (this.parent.getGame().isCompleted() || this.parent.getLastPlayerActionBeforeAutoplay().length() > 0){
     		 String secondLine = lastActionText; ;
	    	 
	    	 if (this.parent.getGame().isCompleted()){
	    		// if (!this.isTablet){
	    			 String opponentTiles = this.parent.getString(R.string.upper_gap_opponent_tiles_0);
	    			 List<String> tiles = this.parent.getGame().getOpponentGame().getTrayLetters();
	    			 if (tiles.size() == 1){
	    				 opponentTiles = String.format(this.parent.getString(R.string.upper_gap_opponent_tiles_1), tiles.get(0));
	    			 }
	    			 else if (tiles.size() == 2){
	    				 opponentTiles = String.format(this.parent.getString(R.string.upper_gap_opponent_tiles_2), tiles.get(0), tiles.get(1));
	    			 }
	    			 else if (tiles.size() == 3){
	    				 opponentTiles = String.format(this.parent.getString(R.string.upper_gap_opponent_tiles_3), tiles.get(0), tiles.get(1), tiles.get(2));
	    			 }
	    			 else if (tiles.size() == 4){
	    				 opponentTiles = String.format(this.parent.getString(R.string.upper_gap_opponent_tiles_4), tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3));
	    			 }
	    			 else if (tiles.size() == 5){
	    				 opponentTiles = String.format(this.parent.getString(R.string.upper_gap_opponent_tiles_5), tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3), tiles.get(4));
	    			 }
	    			 else if (tiles.size() == 6){
	    				 opponentTiles = String.format(this.parent.getString(R.string.upper_gap_opponent_tiles_6), tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3), tiles.get(4), tiles.get(5));
	    			 }
	    			 else if (tiles.size() == 7){
	    				 opponentTiles = String.format(this.parent.getString(R.string.upper_gap_opponent_tiles_7), tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3), tiles.get(4), tiles.get(5), tiles.get(6));
	    			 } 
	    			 
		    		 secondLine = String.format(this.parent.getString(R.string.upper_gap_randoms_and_opponent_tiles),
	     				 this.parent.getGame().getRandomConsonants().get(0),
	     				 this.parent.getGame().getRandomConsonants().get(1),
	     				 this.parent.getGame().getRandomConsonants().get(2),
	     				 this.parent.getGame().getRandomConsonants().get(3),
	     				 this.parent.getGame().getRandomVowel(), 
	     				 this.parent.getGame().getOpponent().getName(),
	     				 opponentTiles);
	    	//	 }
	    	//	 else {
	    	//		 //this is a tablet so show third line
	    	//		 secondLine = String.format(this.parent.getString(R.string.upper_gap_randoms),
		    // 				 this.parent.getGame().getRandomConsonants().get(0),
		    // 				 this.parent.getGame().getRandomConsonants().get(1),
		    // 				 this.parent.getGame().getRandomConsonants().get(2),
		    // 				 this.parent.getGame().getRandomConsonants().get(3),
		    // 				 this.parent.getGame().getRandomVowel());
	    	//	 }
	    	 }
     		 
     		 Paint pSecondLine = new Paint();
     		 pSecondLine.setColor(Color.parseColor(this.parent.getString(R.color.game_board_full_view_upper_gap_text)));

     		 pSecondLine.setTextSize(textSize);
     		 pSecondLine.setAntiAlias(true);
     		 pSecondLine.setTypeface(ApplicationContext.getBonusTypeface()); //(this.bonusTypeface);
     	     Rect boundsSecondLine = new Rect();
     	 
     	     pSecondLine.getTextBounds(secondLine, 0, secondLine.length(), boundsSecondLine);	
/*
     	     //find the vertial space not taken up by text lines
      	    int spaceLeftoverAfterTextIsWritten = this.topGapHeight - (boundsRandom.height() + bounds.height());
     	    
      	    //find the equal division of space above between and below the text lines
      	    int gapBetweenText = Math.round(spaceLeftoverAfterTextIsWritten / 3 );

      	    //find the left starting point for the text line
    	     int textLeftRandom =  this.midpoint - (Math.round(boundsRandom.width() / 2));

    	     //random letters text line will start after top gap, last action text line, and next gap
      	    canvas.drawText(randoms, textLeftRandom, bounds.height() + (gapBetweenText * 2), pRandom);
   	    
      	    //last action text will start after top gap
     	     textTop = gapBetweenText;
  */   	     
     	     
     	     int textVerticalFirstThird =  Math.round(this.topGapHeight / 3);
     	     int textVerticalSecondThird =  textVerticalFirstThird * 2;

     	    
     	     int textLeftRandom =  this.midpoint - (Math.round(boundsSecondLine.width() / 2));
     	     //int textTopRandom = textVerticalMidpoint + 2 + (boundsRandom.height() / 2);
     	     int textTopRandom = textVerticalSecondThird + (boundsSecondLine.height() / 2);
     	     
     	     canvas.drawText(secondLine, textLeftRandom, textTopRandom, pSecondLine); 
     	     
     	     pSecondLine.setColor(Color.parseColor(this.parent.getString(R.color.game_board_full_view_upper_gap_front_text)));	     
    	    
     	     canvas.drawText(secondLine, textLeftRandom - 1, textTopRandom - 1, pSecondLine);
    	    
     	     //re-adjust last action text to handle second line for randoms
     	    // = textVerticalMidpoint - 2 - (bounds.height() / 2 );
     	     textTop = textVerticalFirstThird +  (bounds.height() / 4);
     	     
     	    int spaceLeftoverAfterTextIsWritten = this.topGapHeight - (boundsSecondLine.height() + bounds.height());
     	    
     	    Math.round(spaceLeftoverAfterTextIsWritten / 3 );
     	    
     	    
     	 }
	     
	     
	     canvas.drawText(firstLine, textLeft, textTop, p);
     	 p.setColor(Color.parseColor(this.parent.getString(R.color.game_board_full_view_upper_gap_front_text)));	     
	    
     	 canvas.drawText(firstLine, textLeft - 1, textTop - 1, p);
	     //canvas.drawBitmap(this.logo, textLeft, 10, null); ///do not use 10,,,figure out math
	     //Yes. If you want to use the colour definition in the res/colors.xml file with the ID R.color.black, then you can't just use the ID. 
	     //If you want to get the actual colour value from the resources, use paint.setColor(getResources().getColor(R.color.black)); � Matt Gibson Dec 7 '11 at 20:49
	    
	}

	private void drawLowerGap(Canvas canvas){
		if (this.logo == null) { this.LoadExtras(); }
		Paint pGap = new Paint(); 
		pGap.setColor(Color.parseColor(this.parent.getString(R.color.game_board_full_view_lower_gap_bg)));
	    
		pGap.setAntiAlias(true);
	     Rect boundsGap = new Rect();
	     boundsGap.left = 0;
	     boundsGap.right = this.fullWidth;
	     boundsGap.top = this.topGapHeight + this.fullWidth;//this.trayTop - this.bottomGapHeight - TRAY_TOP_BORDER_HEIGHT - 1;
	     boundsGap.bottom = this.trayTop - 1;
		
	     canvas.drawRect(boundsGap, pGap);
	     
	//     Logger.d(TAG, "logo is null=" + (this.logo == null));
	//     Logger.d(TAG, "midpoint  =" +  this.midpoint);
	//     Logger.d(TAG, "topGapHeight  =" +  this.topGapHeight);
	//     Logger.d(TAG, "fullWidth  =" +  this.fullWidth);
	//     Logger.d(TAG, "bottomGapHeight  =" +  this.bottomGapHeight);
 
	     
	     canvas.drawBitmap(this.logo, this.midpoint - (Math.round(this.logo.getWidth() / 2)), this.topGapHeight + this.fullWidth + Math.round(this.bottomGapHeight / 2) - Math.round(this.logo.getHeight() / 2) , null); ///do not use 10,,,figure out math

	///draw  thanks for playing if game is over '''to the right
		    
	}
	
	
	
	private void drawTray(Canvas canvas){
		Logger.d(TAG, "drawTray called");
		
		Paint pBorder = new Paint(); 
		pBorder.setColor(Color.parseColor(this.parent.getString(R.color.game_board_tray_border)));
	    
	    pBorder.setAntiAlias(true);
	  //   p.setTypeface(this.bonusTypeface);
	     Rect boundsBorder = new Rect();
	     boundsBorder.left = 0;
	     boundsBorder.right = this.trayAreaRect.getRight(); //this.fullWidth;
	     boundsBorder.top = this.trayAreaRect.getTop(); //this.trayTop - TRAY_VERTICAL_MARGIN - TRAY_TOP_BORDER_HEIGHT;
	     boundsBorder.bottom = this.trayTop - TRAY_VERTICAL_MARGIN;
	     canvas.drawRect(boundsBorder, pBorder);
	     
	    // this.trayAreaTop = boundsBorder.top;
	     
	//	canvas.drawBitmap(GameSurfaceView.bgTrayBackground, 0, this.trayTop - TRAY_VERTICAL_MARGIN, null);
 
		Paint pBackground = new Paint(); 
		pBackground.setColor(Color.parseColor(this.parent.getString(R.color.tray_background)));
	    
		pBackground.setAntiAlias(true);
	     Rect boundsBackground = new Rect();
	     boundsBackground.left = 0;
	     boundsBackground.right = this.trayAreaRect.getRight(); //this.fullWidth;
	     boundsBackground.top = this.trayTop - TRAY_VERTICAL_MARGIN; //this.trayTop - TRAY_VERTICAL_MARGIN - TRAY_TOP_BORDER_HEIGHT;
	     
	   //  Logger.d(TAG, "drawTray this.trayAreaRect.getBottom()=" + this.trayAreaRect.getBottom());
	     
	     boundsBackground.bottom = boundsBackground.top + this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2);
	     
	     //boundsBackground.bottom = this.trayAreaRect.getBottom();
	     canvas.drawRect(boundsBackground, pBackground);
	 
		
		 
		for (TrayTile tile : this.trayTiles) { 
			 //canvas.drawBitmap(tile.getCurrentBitmap(),tile.getxPosition(), tile.getyPosition(), null);
			 //if (!tile.isDragging() && tile.getCurrentLetter().length() > 0){
			 if (tile.getCurrentLetter().length() > 0){ 
				 canvas.drawBitmap(GameSurfaceView.bgTrayBaseScaled,tile.getxPosition(), tile.getyPosition(), null);
				 //canvas.drawBitmap(tile.getCurrentBitmap(),tile.getxPosition(), tile.getyPosition(), null);
		     	 Paint pLetter = new Paint();
		     	 pLetter.setColor(Color.parseColor(this.parent.getString(R.color.game_board_tray_tile_letter)));
		     	 pLetter.setTextSize(Math.round(this.trayTileSize * .78));
		     	 pLetter.setAntiAlias(true); 
		     	 pLetter.setTypeface(ApplicationContext.getLetterTypeface()); //(this.letterTypeface);
			     Rect boundsLetter = new Rect();
			     Rect boundsLetterHeight = new Rect();
			     
			     //always base vertical dimension on single letter (T).  based on the font, letters of different height were screwing up the even look
			     pLetter.getTextBounds("T", 0, 1, boundsLetterHeight);
			     pLetter.getTextBounds(tile.getCurrentLetter(), 0, tile.getCurrentLetter().length(), boundsLetter);
			     
			     //find the midpoint and scoot over 5% to the left and 5% down
			     int textLeft =  tile.getxPosition() + this.trayTileMidpoint - Math.round(this.trayTileMidpoint * .15f) - (Math.round(boundsLetter.width() / 2));
			     int textTop =  tile.getyPosition() + this.trayTileMidpoint + Math.round(this.trayTileMidpoint * .08f) + (Math.round(boundsLetterHeight.height() / 2));
			     
			     canvas.drawText(tile.getCurrentLetter(), textLeft, textTop, pLetter);
			     
			     Paint pValue = new Paint();
			     pValue.setColor(Color.parseColor(this.parent.getString(R.color.game_board_tray_tile_value)));
			     pValue.setTextSize(Math.round(this.trayTileSize * .25f));
			     pValue.setAntiAlias(true);
			     
			     pValue.setTypeface(Typeface.SANS_SERIF);
			     Rect boundsValue = new Rect();
			     String value = Integer.toString(AlphabetService.getLetterValue(tile.getCurrentLetter()));
			     pValue.getTextBounds(value, 0, value.length(), boundsValue);
			     
			     //find the midpoint and scoot over 5% to the left and 5% down
			     int textLeftValue =  tile.getxPosition() + this.trayTileSize - boundsValue.width() - Math.round(this.trayTileSize * .12f);
			     int textTopValue =  tile.getyPosition() + Math.round(this.trayTileSize * .15f) + (Math.round(boundsValue.height() / 2));
			     
			     canvas.drawText(value, textLeftValue, textTopValue, pValue);
			     
	     	 }
			 else{
				 
				 canvas.drawBitmap(GameSurfaceView.bgTrayEmptyScaled,tile.getxPosition(), tile.getyPosition(), null);
			 }
			 
		 }
		
	}
	public void setHintLetters(PlacedResult placedResult) throws PreconditionException{
		//clear out all placed letters
		String x = "";
		for (TrayTile trayTile : this.trayTiles){
			x += trayTile.getCurrentLetter();
		}
	//	Logger.d(TAG, "setHintLetters before recall tiles= " + x);
		
		for(GameTile tile : this.tiles){
			if (tile.getPlacedLetter().length() > 0){
				this.parent.getGameState().returnLetterToTray(tile.getPlacedLetter(), tile.getId());
			}
			tile.recallLetter();
		}

		x= "";
				
		for (TrayTile trayTile : this.trayTiles){
			x += trayTile.getCurrentLetter();
		}
	//	Logger.d(TAG, "setHintLetters after recall tiles= " + x);
		
		this.LoadTray();

		x= "";
		for (TrayTile trayTile : this.trayTiles){
			x += trayTile.getCurrentLetter();
		}
	//	Logger.d(TAG, "setHintLetters after loadTray= " + x);
		
		for(GameTile tile : placedResult.getPlacedTiles()){
			boolean trayLetterFound = false;
			for (TrayTile trayTile : this.trayTiles){
				//find first tray letter that match placedLetter and clear it from 
				if (trayTile.getCurrentLetter().equals(tile.getPlacedLetter())){
					trayTile.setCurrentLetter("");
					trayLetterFound = true;
					break;
 				}
			}
		 
			Check.Require(trayLetterFound, "setHintLetters letter not found=" + tile.getPlacedLetter());
 
			this.tiles.get(tile.getId()).setPlacedLetter(tile.getPlacedLetter());
		 }
		
		//set game state
		x= "";
		for (TrayTile trayTile : this.trayTiles){
			x += trayTile.getCurrentLetter();
		}
	//	Logger.d(TAG, "setHintLetters after setTiles= " + x);
 		 //clear the active tiles
 		this.clearCurrentTrayTile();
	 
		this.clearDropTargetTrayTile();
		this.clearTargetTile();
	 
		if (this.getDraggingTile() != null) {
			this.getDraggingTile().setDragPending(false);
		}
		
		this.clearDraggingTile();
	
		this.parent.getGameState().resetLettersFromCurrent(tiles, trayTiles);
		///GameStateService.setGameState(this.context, this.parent.getGameState());
		this.LoadTray(); //reloads from the latest game state
		x= "";
		for (TrayTile trayTile : this.trayTiles){
			x += trayTile.getCurrentLetter();
		}
		//Logger.d(TAG, "setHintLetters after loadTray= " + x);
		
		//this.parent.switchToPlay();
		//this.parent.switchToRecall();
		
		this.isZoomed = false;
		
		this.wordHintRedraw = true;
		this.readyToDraw = true;
		this.resetPointsView();
	}
	
	
	public void recallLetters(){
		
		//clear out all placed letters
		for(GameTile tile : this.tiles){
			if (tile.getPlacedLetter().length() > 0){
				this.parent.getGameState().returnLetterToTray(tile.getPlacedLetter(), tile.getId());
			}
			tile.recallLetter();
		}
		
		//this.parent.getGameState().resetLettersFromOriginal(tiles, trayTiles);
		///GameStateService.setGameState(this.context, this.parent.getGameState());
		this.LoadTray(); //reloads from the latest game state
		
		this.recallLettersRedraw = true;
		this.readyToDraw = true;
		this.resetPointsView();

	}
	
	private void setButtonStates(){
		//if a letter drops on the board, hide the shuffle button and display recall button
		//
		//first check says "if the recall button is showing yet the try is not not full after an ACTION_UP event, switch to recall
		
		//Logger.d(TAG, "setButtonStates called isButtonStateInShuffle=" + this.isButtonStateInShuffle + " this.isTrayFull=" + this.isTrayFull());
		
		if (this.isButtonStateInShuffle && !this.isTrayFull()){
			this.parent.switchToRecall();
			this.parent.switchToPlay();
			this.isButtonStateInShuffle = false;
			//Logger.d(TAG, "setButtonStates switchToRecall called isButtonStateInShuffle=" + this.isButtonStateInShuffle);
		}
		else if (!this.isButtonStateInShuffle && this.isTrayFull()){
			this.parent.switchToShuffle();
			this.parent.switchToSkip();
			this.isButtonStateInShuffle = true;
		//	Logger.d(TAG, "setButtonStates switchToShuffle called isButtonStateInShuffle=" + this.isButtonStateInShuffle);
		}
	}
	
	public void setInitialButtonStates(){
		//gameSurface class will default the button to shuffle.
		//we just need to change it here if the tray is not full
		
		//Logger.d(TAG, "setInitialButtonStates called isButtonStateInShuffle=" + this.isButtonStateInShuffle + " this.isTrayFull=" + this.isTrayFull());
		
		if (!this.isTrayFull()){
			this.parent.switchToRecall();
			this.parent.switchToPlay();
			this.isButtonStateInShuffle = false;
		}
	
	}
	
	public void resetGameAfterRefresh(){
		
		//clear out everything and start anew
		this.tiles.clear();
		this.trayTiles.clear();
		
		this.LoadTiles();
	   	this.LoadTray();
	    this.setInitialButtonStates();
	    if (this.parent.getGame().isCompleted()){
	    	this.parent.setPointsView(0);
	    }
	    else {
	    	this.resetPointsView();
	    }
	 
	   
	   // this.afterPlayRedraw = true; ??
	    this.isZoomed = false;
		this.readyToDraw = true;
	 
		// this.resetPointsView();
		}
	
	public void resetGameAfterPlay(){
		//loop through game tiles, changing placed letter to original letter
		//doing it this way keeps the previous drag locations set properly
		for (GameTile tile : this.tiles){
			 //reset all last played flags
            tile.setLastPlayed(false);

			if (tile.getPlacedLetter().length() > 0){
				 tile.setOriginalBitmap(GameSurfaceView.bgBaseScaled); //this will change as default bonus and played tiles are incorporated
				 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bgBaseZoomed); }
				 
				 tile.setOriginalLetter(tile.getPlacedLetter());
				 tile.setPlacedLetter("");
				 tile.setLastPlayed(true);
			}
		}
		
	   	this.LoadTray();
	    this.setInitialButtonStates();
	   
	    //if game ends between turns, make sure to zoom out
	    if (this.parent.getGame().isCompleted()){
	    	this.isZoomed = false;
	    }
	    this.afterPlayRedraw = true;
		this.readyToDraw = true;
		
		this.parent.setPointsAfterPlayView();
		// this.resetPointsView();
		}
	
	public void clearPlacedTiles(){
		for(GameTile tile : this.tiles){
			if (tile.getPlacedLetter().length() > 0){
				tile.removePlacement();
			}
		}
	}
	
	public void onPlayClick(){
		try{
			this.parent.captureTime("onPlayClick checkPlayRules starting");
			//final PlacedResult placedResult = GameService.checkPlayRules(parent, this.defaultLayout, this.parent.getGame(), this.tiles, this.trayTiles, false);
			final PlacedResult placedResult = GameService.checkPlayRules(parent, this.defaultLayout, this.parent.getGame(), this.tiles, false);
			
			this.parent.onFinishPlayNoErrors(placedResult);

		}
		catch (DesignByContractException e){
			
			this.parent.onFinishPlayErrors(e.getMessage(), e.getErrorCode());
			//this.parent.openAlertDialog(this.parent.getString(R.string.sorry), e.getMessage());
			//this.parent.unfreezeButtons();
		}
	}
	
	public void postPlayNoErrors_____(final PlacedResult placedResult ){
		this.parent.setPointsView(placedResult.getTotalPoints());
		
		//this.parent.handleGamePlayOnClick(placedResult);
		
		//if (placedResult.getPlacedTiles().size() == 800){
		if (placedResult.getPlacedTiles().size() == 0){
			
			/*	View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
				Button btn = (Button)view.findViewById(R.id.the_id_of_the_button);
				btn.setOnClickListener(blah blah);
				AlertDialog dialog = new AlertDialog.Builder(context)
				    .setView(view)
				    .create();
				*/
				//user is skipping this turn
				this.dialog = new CustomButtonDialog(parent, 
						parent.getString(R.string.game_play_skip_title), 
						parent.getString(R.string.game_play_skip_confirmation_text),
						parent.getString(R.string.yes),
						parent.getString(R.string.no));
		    	/*
		    	dialog.setOnOKClickListener(new View.OnClickListener() {
			 		@Override
					public void onClick(View v) {
			 			//dialog.dismiss(); 
			 			dismissDialog();
			 			parent.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_SKIP_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			 			
			 			parent.handleGameSkipOnClick();
			 		}
				});
		    	
		    	dialog.setOnDismissListener(new View.OnClickListener() {
			 		@Override
					public void onClick(View v) {
			 			//dialog.dismiss();
			 			dismissDialog();
			 	 		parent.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_SKIP_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			 			parent.unfreezeButtons();
			 			
			 		}
				});
		    	
		    	 
		    	dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						parent.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_SKIP_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
						
						parent.unfreezeButtons();
						
					}
				});
				 */
		    	dialog.show();
			}
			else{
				//loop through placed words and show confirmation messages 
				this.dialog = new CustomButtonDialog(parent, 
						parent.getString(R.string.game_play_title), 
		    			GameService.getPlacedWordsMessage(parent, placedResult.getPlacedWords()),
		    			parent.getString(R.string.yes),
		    			parent.getString(R.string.no),
		    			R.layout.played_word_dialog);
		    	/*
		    	dialog.setOnOKClickListener(new View.OnClickListener() {
			 		@Override
					public void onClick(View v) {
			 			//dialog.dismiss(); 
			 			dismissDialog();
			 			parent.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_PLAY_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			 			
			 			parent.handleGamePlayOnClick(placedResult);
			 		}
				});
	
		    	dialog.setOnDismissListener(new View.OnClickListener() {
			 		@Override
					public void onClick(View v) {
			 			//dialog.dismiss();
			 			dismissDialog();
			 			parent.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_PLAY_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			 			
			 			parent.unfreezeButtons();
			 		}
				});
		    	 
		    	dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						parent.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_PLAY_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
						
						parent.unfreezeButtons();
						
					}
				});
		    	*/
		    	dialog.show();
			}
		//}
	}
	
	private void onBoardClear(){
		//
		this.parent.switchToShuffle();
	}
	
	public boolean isTrayFull(){
		int numLettersInTray = 0;
		for(TrayTile tile : this.trayTiles){
			if (tile.getCurrentLetter().length() > 0){
				numLettersInTray += 1;
			}
		}
		//Logger.d(TAG, "isTrayFull numLettersInTray=" + numLettersInTray);  
		//Logger.d(TAG, "isTrayFull this.parent.getGameState().getLocations().size()=" + this.parent.getGameState().getLocations().size());
		//Logger.d(TAG, "isTrayFull this.parent.contextPlayerGame.getTrayLetters().size()=" + this.parent.contextPlayerGame.getTrayLetters().size());
		
		//some letters are out of the tray, only recall is allowed now, not shuffling
		return (numLettersInTray == this.parent.getGame().getPlayerGames().get(0).getTrayLetters().size()); //this.parent.getGameState().getLocations().size());
	}
	
	public void shuffleTray(){
		Logger.d(TAG, "shuffleTray called");
		//make sure that tray is full of letters (the number that are left in the player's tray from the server)
		if (!this.isTrayFull()){
			return;
		}

		this.parent.getGameState().shuffleLetters();
	
		///GameStateService.setGameState(this.context, this.parent.getGameState());

		this.LoadTray();
		this.shuffleRedraw = true;
		this.readyToDraw = true;
		
	}
	
	private void LoadExtras(){
		
		//check to see if this has already run
		if (this.logo != null) { return; }
		Logger.d(TAG, "LoadExtras starting");
		
		int gameBoardLogoHeightPercentageInTenths = this.parent.getResources().getInteger(R.integer.gameBoardLogoHeightPercentageInTenths);
		
		//int height = Math.round(this.bottomGapHeight * .9F);  //make this percentage an integer 
		int height = Math.round(this.bottomGapHeight * (float)(gameBoardLogoHeightPercentageInTenths / 10.0 ));  //make this percentage an integer 
		
		Logger.d(TAG, "LoadExtras gameBoardLogoHeightPercentageInTenths=" + this.parent.getResources().getInteger(R.integer.gameBoardLogoHeightPercentageInTenths) + " height=" + height + " bottomGapHeight=" + this.bottomGapHeight);
		
		
		int maxLogoHeight = this.parent.getResources().getInteger(R.integer.maxGameBoardLogoHeight);
		if (height > maxLogoHeight){
			height = maxLogoHeight; 
		}
		//Logger.d(TAG, "loadExtras logo height=" + height);
		Bitmap bgLogo = BitmapFactory.decodeResource(getResources(), R.drawable.gameboard_logo); 
	 
		float factor = height / (float) bgLogo.getHeight(); 
		float width = bgLogo.getWidth() * factor;
		// this.logo = Bitmap.createScaledBitmap(bgLogo, (int) (bgLogo.getWidth() * factor), height, false); 
		
		this.logo = ImageHelper.getResizedBitmap(bgLogo, (int)width, height);
		bgLogo = null;
	} 
	
	private void LoadTray() {		

		//move bitmap stuff to shared location
	//	Bitmap bgTray = BitmapFactory.decodeResource(getResources(), R.drawable.sbd_bg);
	//	this.trayBackground = Bitmap.createScaledBitmap(bgTray, this.fullWidth, this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2), false);
		 
		
	//	 Bitmap bgBase = BitmapFactory.decodeResource(getResources(), R.drawable.tray_tile_bg);
	//	 Bitmap bgTrayBaseScaled = Bitmap.createScaledBitmap(bgBase, this.trayTileSize , this.trayTileSize, false);
	//	 Bitmap bgTrayBaseDragging = Bitmap.createScaledBitmap(bgBase, this.draggingTileSize, this.draggingTileSize, false);
		
		 //load game letters into here (soon)
		 
		//take game state into account here, 
		
		
		 this.trayTiles.clear();
		 
		 for(int y = 0; y < 7; y++){
			 TrayTile tile = new TrayTile();
			 tile.setId(y);
			 tile.setxPosition(this.trayTileLeftMargin + ((this.trayTileSize + TRAY_TILE_GAP) * tile.getId()));
			 tile.setyPosition(this.trayTop);
			 
			 tile.setxPositionCenter(Math.round(tile.getxPosition() + (this.trayTileSize / 2)));
			 tile.setyPositionCenter(Math.round(tile.getyPosition() + (this.trayTileSize / 2)));

			//// tile.setOriginalBitmap(GameSurfaceView.bgTrayBaseScaled);
			 
			// Logger.d(TAG, "LoadTray this.parent.getGameState().getTrayLetter(y)=" + this.parent.getGameState().getTrayLetter(y));
			 
			 //this will come from state object if it exists for this turn, this is temp
			 tile.setOriginalLetter(this.parent.getGameState().getTrayLetter(y)); //getTrayTiles().get(y).getLetter());
			 
			 this.trayTiles.add(tile);
		 }
	}
	
	 private void LoadTiles() {		  
		 //for each row load each column 15x15
		 //row = y
	     //column = x
		 int id = 0;
		 for(int y = 0; y < 15; y++){
			 for(int x = 0; x < 15; x++){
				 GameTile tile = new GameTile();
				 tile.setId(id);
				 id += 1;
				 tile.setxPosition((this.excessWidth / 2) + (x * this.fullViewTileWidth) + (x * this.tileGap));
				 tile.setyPosition((y * this.fullViewTileWidth) + (y * this.tileGap) + this.topGapHeight);
				 tile.setColumn(x + 1);
				 tile.setRow(y + 1);
				 
				 tile.setxPositionCenterRelativeZoomed((tile.getColumn() * this.zoomedTileWidth) + Math.round(this.zoomedTileWidth / 2));
				 tile.setyPositionCenterRelativeZoomed((tile.getRow() * this.zoomedTileWidth) + Math.round(this.zoomedTileWidth / 2));
								 
				 //set center of the tile.  this will be used for drop assignments later as the player
				 //drops tiles on the board
				 tile.setxPositionCenter(tile.getxPosition() + (this.fullViewTileWidth / 2));
				 tile.setyPositionCenter(tile.getyPosition() + (this.fullViewTileWidth / 2));
				 
				 //check game object for already played letter
				 PlayedTile playedTile = this.parent.getGameState().getPlayedTileByPosition(tile.getId());
				 if (playedTile != null){
					 //if (id == 196){
					//	 Logger.d(TAG, "LoadTiles playedTile 196 " + playedTile.getLatestPlayedLetter());
					 //}
					  
					 tile.setOriginalBitmap(bgBaseScaled); //this will change as default bonus and played tiles are incorporated
					 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bgBaseZoomed); }
	
					 //tile.setOriginalLetter(playedTile.getLetter());
					 
				//	 if (id == 196){
				//		 Logger.d(TAG, "LoadTiles 196 " + playedTile.getLatestPlayedLetter().getLetter());
				//	 }
					 
					 tile.setOriginalLetter(playedTile.getLatestPlayedLetter().getLetter());

					 //was this tile placed in the previous turn???
					 if (this.parent.getGame().getTurn() - 1 == playedTile.getLatestPlayedLetter().getTurn()){
						 tile.setLastPlayed(true);
					 }
				 }
				 else {
					 //check defaultLayout for bonus tiles etc
					 switch (TileLayoutService.getDefaultTile(tile.getId(), this.defaultLayout)) {
					 case FourLetter:
						 tile.setOriginalBitmap(bg4LScaled); //this will change as default bonus and played tiles are incorporated
						 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bg4LZoomed); }
						 tile.setOriginalText(this.parent.getString(R.string.tile_4L));
						 break;
					 case ThreeWord:
						 tile.setOriginalBitmap(bg3WScaled); //this will change as default bonus and played tiles are incorporated
						 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bg3WZoomed); }
						 tile.setOriginalText(this.parent.getString(R.string.tile_3W));
						 break;
					 case ThreeLetter:
						 tile.setOriginalBitmap(bg3LScaled); //this will change as default bonus and played tiles are incorporated
						 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bg3LZoomed); }
						 tile.setOriginalText(this.parent.getString(R.string.tile_3L));
						 break;
					 case TwoWord:
						 tile.setOriginalBitmap(bg2WScaled); //this will change as default bonus and played tiles are incorporated
						 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bg2WZoomed); }
						 tile.setOriginalText(this.parent.getString(R.string.tile_2W));
						 break;
					 case TwoLetter:
						 tile.setOriginalBitmap(bg2LScaled); //this will change as default bonus and played tiles are incorporated
						 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bg2LZoomed); }
						 tile.setOriginalText(this.parent.getString(R.string.tile_2L));
						 break;
					 case Starter:
						 //make starter tiles disappear after first word is on board
						 if (this.parent.getGame().getPlayedWords().size() == 0){
							 tile.setOriginalBitmap(bgStarterScaled); //this will change as default bonus and played tiles are incorporated
							 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bgStarterZoomed); }
						 }
						 else{
							 tile.setOriginalBitmap(bgBaseScaled); //this will change as default bonus and played tiles are incorporated
							 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bgBaseZoomed); }							 
						 }
						 break;
					 case None:
						 tile.setOriginalBitmap(bgBaseScaled); //this will change as default bonus and played tiles are incorporated
						 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(GameSurfaceView.bgBaseZoomed); }
						 break;
					 }
				 }
				 
				// if (id == 196){
				//	 Logger.d(TAG, "LoadTiles NO playedTile 196 ");
				// }

				 if (this.parent.getGameState().getBoardLetter(tile.getId()).length() > 0){
					 
					 Logger.d(TAG, "LoadTiles PLACED 196 " + this.parent.getGameState().getBoardLetter(tile.getId()));
					 
					  tile.setPlacedLetter(this.parent.getGameState().getBoardLetter(tile.getId()));
				 }
				 
				 this.tiles.add(tile);
			 }
		 }
		 
	//	 this.loadTileNeighbors();//??????
	 }
	 /*
	 private void loadTileNeighbors(){
		 int i = 0;
		 for(int y = 0; y < 15; y++){
			 
			 for(int x = 0; x < 15; x++){
				 int above = TileLayoutService.getTileIdAbove(i);
				 int below = TileLayoutService.getTileIdBelow(i);
				 int left = TileLayoutService.getTileIdToTheLeft(i);
				 int right = TileLayoutService.getTileIdToTheRight(i);
				 
				 if (below < 255) {this.tiles.get(i).setTileBelow(this.tiles.get(below));}
				 if (above < 255) {this.tiles.get(i).setTileAbove(this.tiles.get(above));}
				 if (right < 255) {this.tiles.get(i).setTileToRight(this.tiles.get(left));}
				 if (left < 255) {this.tiles.get(i).setTileToLeft(this.tiles.get(right));}
				 
				 i += 1;
			 }
		 }

	 }
	 */
	 
	 private void setFullViewRegionPosition(GameTile tile){
		 //region1 top left third
		 //ids=0,1,2,3,4.. 15,16,17,18,19...30,31,32,33,34...45,46,47,48,49...60,61,62,63,64
		 //corners= top left=0, top right=4 , bottom left=60 , bottom right=64
		 
		 //region2 top center third
		 //ids=5,6,7,8,9.. 20,21,22,23,24...35,36,37,38,39...50,51,52,53,54...65,66,67,68,69
		 //corners= top left=5, top right=9 , bottom left=65 , bottom right=69
		 
		 //region3 top right third
		 //ids=10,11,12,13,14.. 25,26,27,28,29...40,41,42,43,44...55,56,57,58,59...70,71,72,73,74
		 //corners= top left=10, top right=14 , bottom left=70 , bottom right=74
		 
		 //region4 center left third
		 //ids=75,76,77,78,79.. 90,91,92,93,94...105,106,107,108,109...120,121,122,123,124...135,136,137,138,139
		 //corners= top left=75, top right=79 , bottom left=135 , bottom right=139
		 
		 //region5 center center third
		 //ids=80,81,82,83,84... 95,96,97,98,99...110,111,112,113,114...125,126,127,128,129...140,141,142,143,144
		 //corners= top left=80, top right=84 , bottom left=140 , bottom right=144
		 
		 //region6 center right third
		 //ids=85,86,87,88,89... 100,101,102,103,104...115,116,117,118,119...130,131,132,133,134...145,146,147,148,149
		 //corners= top left=85, top right=89 , bottom left=145 , bottom right=149
		 
		 //region7 bottom left third
		 //ids=150,151,152,153,154... 165,166,167,168,169...180,181,182,183,184...195,196,197,198,199...210,211,212,213,214
		 //corners= top left=150, top right=154 , bottom left=210 , bottom right=214
		 
		 //region8 bottom center third
		 //ids=155,156,157,158,159... 170,171,172,173,174...185,186,187,188,189...200,201,202,203,204...215,216,217,218,219
		 //corners= top left=155, top right=159 , bottom left=215 , bottom right=219

		 //region9 bottom center third
		 //ids=160,161,162,163,164... 175,176,177,178,179...190,191,192,193,194...205,206,207,208,209...220,221,222,223,224
		 //corners= top left=160, top right=164 , bottom left=220 , bottom right=224
		 
		 switch (tile.getId())
		 {
		 case 0: //top left of region 1
			 this.region1FullViewRect.setLeft(tile.getxPosition());
			 this.region1FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	 case 64: //bottom right of region 1
	 		this.region1FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region1FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
		 case 5: //top left of region 2
			 this.region2FullViewRect.setLeft(tile.getxPosition());
			 this.region2FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	 case 69: //bottom right of region 2
	 		this.region2FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region2FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
	 	case 10: //top left of region 3
			 this.region3FullViewRect.setLeft(tile.getxPosition());
			 this.region3FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	 case 74: //bottom right of region 3
	 		this.region3FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region3FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
	  	case 75: //top left of region 4
			 this.region4FullViewRect.setLeft(tile.getxPosition());
			 this.region4FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	 case 139: //bottom right of region 4
	 		this.region4FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region4FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
	  	case 80: //top left of region 5
			 this.region5FullViewRect.setLeft(tile.getxPosition());
			 this.region5FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	 case 144: //bottom right of region 5
	 		this.region5FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region5FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
	  	case 85: //top left of region 6
			 this.region6FullViewRect.setLeft(tile.getxPosition());
			 this.region6FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	 case 149: //bottom right of region 6
	 		this.region6FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region6FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
	  	case 150: //top left of region 7
			 this.region7FullViewRect.setLeft(tile.getxPosition());
			 this.region7FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	case 214: //bottom right of region 7
	 		this.region7FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region7FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
	 	case 155: //top left of region 8
			 this.region8FullViewRect.setLeft(tile.getxPosition());
			 this.region8FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	case 219: //bottom right of region 8
	 		this.region8FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region8FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
	 	case 160: //top left of region 9
			 this.region9FullViewRect.setLeft(tile.getxPosition());
			 this.region9FullViewRect.setTop(tile.getyPosition());		 
			 break;
	 	case 224: //bottom right of region 9
	 		this.region9FullViewRect.setRight(tile.getxPosition() + this.fullViewTileWidth + this.tileGap);  
	 		this.region9FullViewRect.setBottom(tile.getyPosition() + this.fullViewTileWidth + this.tileGap);	
	 		break;
		 }
		 
	 }
	 //private GameTile FindTileFromPositionInZoomedMode(int xPosition, int yPosition){
	 private int FindTileFromPositionInZoomedMode(int xPosition, int yPosition){
		 
		 ///look for x and y position values coming in.
		 
		 int tileId = -1;
//		 Logger.d(TAG, "FindTileFromPositionInZoomedMode xPosition=" + xPosition + " yPosition=" + yPosition);
		 
//		 Logger.d(TAG, "FindTileFromPositionInZoomedMode this.boardAreaRect top=" + this.boardAreaRect.getTop() 
//				 + " bottom=" + this.boardAreaRect.getBottom() 
//				 + " left=" + this.boardAreaRect.getLeft() 
//				 + " right=" + this.boardAreaRect.getRight());
		 this.parent.captureTime(TAG + " FindTileFromPositionInZoomedMode loop starting");
		 
		 for (GameTile tile : this.tiles) { 
	    	  //if (xPosition >= tile.getxPositionZoomed() && xPosition <= tile.getxPositionZoomed() + this.zoomedTileWidth + this.tileGap &&
	    	  //		 yPosition >= tile.getyPositionZoomed() && yPosition <= tile.getyPositionZoomed() + this.zoomedTileWidth + this.tileGap){
	    		 //in zoomed mode do not find tiles that are fully below the tray area
	    		 //	public RectArea(int top, int left, int bottom, int right){
		
    		 	// if (tile.getyPositionZoomed() < this.trayAreaRect.getTop()){
	    	 	//	 return tile.getId();	 
	    	 	// }
		 	//}	  
			 //in zoomed mode, only check the area of the tile that falls within the board area
			 RectArea overlapWithBoard = this.boardAreaRect.getAreaWithinBounds(new RectArea(tile.getyPositionZoomed(), tile.getxPositionZoomed(), tile.getyPositionZoomed() + this.zoomedTileWidth + this.tileGap, tile.getxPositionZoomed() + this.zoomedTileWidth  + this.tileGap));
			// Logger.d(TAG, "FindTileFromPositionInZoomedMode " + tile.getId() + " " + (overlapWithBoard != null));
	
			 
//			 Logger.d(TAG, "FindTileFromPositionInZoomedMode tile top=" + tile.getyPositionZoomed()
//					 + " bottom=" + (tile.getyPositionZoomed() + this.zoomedTileWidth + this.tileGap)
//					 + " left=" + tile.getxPositionZoomed()
//					 + " right=" + (tile.getxPositionZoomed() + this.zoomedTileWidth  + this.tileGap));

			 if (overlapWithBoard != null){
//			 		Logger.d(TAG, "FindTileFromPositionInZoomedMode overlapWithBoard top=" + overlapWithBoard.getTop() 
//					+ " bottom=" + overlapWithBoard.getBottom()
//					+ " height=" + (overlapWithBoard.getBottom() - overlapWithBoard.getTop())
//					+ " left=" + overlapWithBoard.getLeft() 
//					+ " right=" + overlapWithBoard.getRight() 
//					+ " width=" + (overlapWithBoard.getRight() - overlapWithBoard.getLeft()));
			 
			 
			
    			 if (xPosition >= overlapWithBoard.getLeft() && xPosition <= overlapWithBoard.getRight() &&
    	    	 		 yPosition >= overlapWithBoard.getTop() && yPosition <= overlapWithBoard.getBottom()){
    				 
 //   				 Logger.d(TAG, "FindTileFromPositionInZoomedMode match found=" + tile.getId());
    				 
    				 return tile.getId();	 
    			 }
	    	 }
			 
			 
		//	 else{
	//			 Logger.d(TAG, "FindTileFromPositionInZoomedMode " + tile.getId() + " is fully outside of the given boundary");
	//		 }

	     }
		 
		 this.parent.captureTime(TAG + " FindTileFromPositionInZoomedMode loop ended");
		// Logger.d(TAG, "FindTileFromPositionInZoomedMode tileId match=" + tileId);
		 return tileId; //null;
	 }
	 
	 //private GameTile FindTileFromPositionInFullViewMode(int xPosition, int yPosition){
	 private int FindTileFromPositionInFullViewMode(int xPosition, int yPosition){
		 int tileId = -1;
		 this.parent.captureTime(TAG + " FindTileFromPositionInFullViewMode loop starting");
		 
		 for (GameTile tile : this.tiles) { 
	    	 if (xPosition >= tile.getxPosition() && xPosition <= tile.getxPosition() + this.fullViewTileWidth + this.tileGap &&
	    	 		 yPosition >= tile.getyPosition() && yPosition <= tile.getyPosition() + this.fullViewTileWidth + this.tileGap){
	    		 return tile.getId();
	    	 }
	    	}
		 
		 this.parent.captureTime(TAG + " FindTileFromPositionInFullViewMode loop ended");
		 return tileId; //null;
	 }
	 
	 //private TrayTile FindTrayTileFromPosition(int xPosition, int yPosition){
	private int FindTrayTileFromPosition(int xPosition, int yPosition){
		int tileId = -1;
		 //Logger.d(TAG, "FindTrayTileFromPositionInFullViewMode xPosition=" + xPosition + " yPosition=" + yPosition);
		 for (TrayTile tile : this.trayTiles) { 
			// Logger.d(TAG, "FindTrayTileFromPosition letter=" + tile.getVisibleLetter() + " xPosition=" + tile.getxPosition() + " yPosition=" + tile.getyPosition());
	    	 if (xPosition >= tile.getxPosition() && xPosition <= tile.getxPosition() + this.trayTileSize + TRAY_TILE_GAP &&
	    	 		 yPosition >= tile.getyPosition() && yPosition <= tile.getyPosition() + this.trayTileSize + TRAY_TILE_GAP){
	    		// Logger.d(TAG, "FindTrayTileFromPositionInFullViewMode MATCH letter=" + tile.getVisibleLetter());
	    		    
	    		 return tile.getId();
	    	 }
	    	}
		 return tileId;
	 }

	 private void drawFullView(Canvas canvas){
		 
		//first apply the background color
		Paint pBoard = new Paint(); 
		pBoard.setColor(Color.parseColor(this.parent.getString(R.color.game_board_full_view_board_bg)));  
	    
		pBoard.setAntiAlias(true);
	  //   p.setTypeface(this.bonusTypeface);
	     Rect boundsBoard = new Rect(); 
	     boundsBoard.left = 0;
	     boundsBoard.right = this.fullWidth;
	     boundsBoard.top = this.topGapHeight;
	     boundsBoard.bottom = this.topGapHeight + this.fullWidth;
		
	     canvas.drawRect(boundsBoard, pBoard);
		 
        for (GameTile tile : this.tiles) { 
	    	 
	    	 
        	 if (tile.getPlacedLetter().length() > 0){
	    		 canvas.drawBitmap(GameSurfaceView.bgPlacedTileFull,tile.getxPosition(), tile.getyPosition(), null);
	    		 if(tile.getId() == 196) {
	    			 
    				 Logger.d(TAG, "drawFullView placed=" + tile.getPlacedLetter());
    		 }

	    		 this.drawLetter(canvas, tile.getPlacedLetter(), this.fullViewTileWidth, tile.getxPosition(), tile.getyPosition(), false);
	    	
	    	 }
        	 else if (tile.getOriginalLetter().length() > 0){
	    		 canvas.drawBitmap(tile.isLastPlayed() ? GameSurfaceView.bgLastPlayedTileFull : GameSurfaceView.bgPlayedTileFull,tile.getxPosition(), tile.getyPosition(), null);
	    		 if(tile.getId() == 196 && tile.isLastPlayed()) {
	    			 
    				 Logger.d(TAG, "drawFullView lastplayed =" + tile.getOriginalLetter());
    		 }
 if(tile.getId() == 196 && !tile.isLastPlayed()) {
	    			 
    				 Logger.d(TAG, "drawFullView played =" + tile.getOriginalLetter());
    		 }

	    		 this.drawLetter(canvas, tile.getOriginalLetter(), this.fullViewTileWidth, tile.getxPosition(), tile.getyPosition(), tile.isLastPlayed());
	    	
	    	 } 
        	 else if (tile.getOriginalText().length() > 0){
	    		 canvas.drawBitmap(tile.getOriginalBitmap(),tile.getxPosition(), tile.getyPosition(), null);
	    		 
	    		 this.drawBonusText(canvas, tile.getOriginalText(), this.fullViewTileWidth, tile.getxPosition(), tile.getyPosition());
	    		 
	    	 }
	    	 else {
	    		 if(tile.getId() == 196) {
	    			 
    				 Logger.d(TAG, "drawFullView empty=" + tile.getPlacedLetter());
    		 }
	    		 canvas.drawBitmap(tile.getOriginalBitmap(),tile.getxPosition(), tile.getyPosition(), null);
	    	 }
	    	 
	    }
        this.readyToDraw = false; 
	 }
	 
	 private void drawBonusText(Canvas canvas, String text, int tileWidth, int xPosition, int yPosition){
		 int midPoint = Math.round(tileWidth / 2);
		 Paint p = new Paint(); 
    	 p.setColor(Color.parseColor(this.parent.getString(R.color.game_board_bonus_text)));
	     p.setTextSize(Math.round(tileWidth * .6));
	     p.setAntiAlias(true);
	     p.setTypeface(ApplicationContext.getBonusTypeface()); //(this.bonusTypeface);
	     Rect bounds = new Rect();
	     p.getTextBounds(text, 0, text.length(), bounds);
	     int textLeft =  xPosition + midPoint - (Math.round(bounds.width() / 2));
	     int textTop =  yPosition + midPoint + (Math.round(bounds.height() / 2));
	     
	     canvas.drawText(text, textLeft, textTop, p);
	 }
	 
	 private void drawLetter(Canvas canvas, String letter, int tileWidth, int xPosition, int yPosition, boolean isLastPlayed){
		 	int midPoint = Math.round(tileWidth / 2);
		  Paint pLetter = new Paint();
	     	 pLetter.setColor(Color.parseColor(isLastPlayed ? this.parent.getString(R.color.game_board_board_last_played_tile_letter) : this.parent.getString(R.color.game_board_board_tile_letter))); //(Color.DKGRAY);
	     	 pLetter.setTextSize(Math.round(tileWidth * .78));
	     	 pLetter.setAntiAlias(true); 
	     	 pLetter.setTypeface(ApplicationContext.getLetterTypeface()); //(this.letterTypeface);
		     Rect boundsLetter = new Rect();
		     Rect boundsLetterHeight = new Rect();
		     
		     //always base vertical dimension on single letter (T).  based on the font, letters of different height were screwing up the even look
		     pLetter.getTextBounds("T", 0, 1, boundsLetterHeight);
		     pLetter.getTextBounds(letter, 0, letter.length(), boundsLetter);
		     
		     //find the midpoint and scoot over 5% to the left and 5% down
		     int textLeft =  xPosition + midPoint - Math.round(midPoint * .15f) - (Math.round(boundsLetter.width() / 2));
		     int textTop =  yPosition + midPoint + Math.round(midPoint * .08f) + (Math.round(boundsLetterHeight.height() / 2));
		     
		     canvas.drawText(letter, textLeft, textTop, pLetter);
		     
		     Paint pValue = new Paint();
		      pValue.setColor(Color.parseColor(isLastPlayed ? this.parent.getString(R.color.game_board_board_last_played_tile_value) : this.parent.getString(R.color.game_board_board_tile_value)));;
		     pValue.setTextSize(Math.round(tileWidth * .25f));
		     pValue.setAntiAlias(true);
		     
		     pValue.setTypeface(Typeface.SANS_SERIF);
		     Rect boundsValue = new Rect();
		     String value = Integer.toString(AlphabetService.getLetterValue(letter));
		     pValue.getTextBounds(value, 0, value.length(), boundsValue);
		     
		     //find the midpoint and scoot over 5% to the left and 5% down
		     int textLeftValue =  xPosition + tileWidth - boundsValue.width() - Math.round(tileWidth * .12f);
		     int textTopValue =  yPosition + Math.round(tileWidth * .20f) + (Math.round(boundsValue.height() / 2));
		     
		     canvas.drawText(value, textLeftValue, textTopValue, pValue);
		     
	 }
	 
}
