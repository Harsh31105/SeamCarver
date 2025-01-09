import java.awt.*;
import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;

// This represents a pixel of the image, represented as one of either a colored pixel of the image
// or a defined border pixel that behaves as a black, opaque pixel present on the borders of the
// image pixels, surrounding the colored pixels.
interface IPixel {
  // This method computes and returns the 'brightness' of this IPixel, which is the arithmetic
  // mean of the pixel's color's RGB values, divided by 255.0, to always attain a number
  // from 0.0 and 1.0.
  double calculateBrightness();

  // This method computes the vertical energy component of an IPixel on one side, given that
  // this IPixel is the direct neighbour of the IPixel whose vertical energy we wish to calculate.
  double findVEComponent();

  // This method computes the horizontal energy component of an IPixel on one side, given that
  // this IPixel is the direct neighbour of the IPixel whose horizontal energy we wish to
  // calculate.
  double findHEComponent();

  // This method sets the given APixels up, down, left and right as the neighbouring APixels of
  // this IPixel; their up-, down-, left- and right- neighbours respectively.
  // EFFECT: Sets the neighbors of this IPixel to the given APixels
  void makeConnectionsPixel(APixel up, APixel down, APixel left, APixel right);

  // This method uses this IPixel's horizontal and vertical energies to compute this IPixel's
  // energy.
  double calculateEnergy();

  // This method checks the invariant property of this IPixel and its neighbours: that the diagonal
  // neighbours of this IPixel are the respective neighbours of this IPixel's neighbours.
  // Ex - this IPixel's top-neighbour's left-neighbour is the same as this IPixel's
  // left-neighbour's top-neighbour.
  boolean isWellFormed();

  // This method computes the updated energies of this APixel in the case that any of its
  // neighbours have been updated.
  // EFFECT: updates the energies of this APixel
  void updateEnergies();

  // EFFECT: reconnects a pixel that was part of a seam that was removed
  void reconnectPixel();

  // finds the start of the image in the given grid
  APixel findImageStart();

  // Confirms whether or not the APixel has a valid down-neighbour.
  boolean hasDown();

  // Confirms whether or not the APixel has a valid right-neighbour.
  boolean hasRight();

  // Finds and returns the up neighbor of this pixel
  APixel upNeighbor();

  // Finds and returns the down neighbor of this pixel
  APixel downNeighbor();

  // Finds and returns the left neighbor of this pixel
  APixel leftNeighbor();

  // Finds and returns the right neighbor of this pixel
  APixel rightNeighbor();

  // Finds and returns the color displayed of this pixel
  Color checkColorDisplayed();

  // EFFECT: changes the colDisplayed field of this pixel
  void makeColorDisplayedTo(Color col);
}

// This class is a generic representation of an IPixel, describing features and methods that are
// applicable to both Pixel and Border.
abstract class APixel implements IPixel {
  Color col;
  Color colDisplayed;
  APixel up;
  APixel down;
  APixel left;
  APixel right;

  // Constructor
  APixel(Color col, APixel up, APixel down, APixel left, APixel right) {
    this.col = col;
    this.colDisplayed = col;
    up.down = this;
    down.up = this;
    left.right = this;
    right.left = this;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
    up.updateEnergies();
    down.updateEnergies();
    left.updateEnergies();
    right.updateEnergies();
    this.updateEnergies();
  }

  APixel(Color col) {
    this.col = col;
    this.colDisplayed = col;
  }

  // This method computes and returns the 'brightness' of this APixel, which is the arithmetic
  // mean of the pixel's color's RGB values, divided by 255.0, to always attain a number
  // from 0.0 and 1.0.
  public double calculateBrightness() {
    return ((this.col.getRed() + this.col.getGreen() + this.col.getBlue()) / 3.0) / 255.0;
  }

  // This method computes the vertical energy component of an IPixel on one side, given that
  // this APixel is the direct neighbour of the IPixel whose vertical energy we wish to calculate.
  public abstract double findVEComponent();

  // This method computes the horizontal energy component of an IPixel on one side, given that
  // this APixel is the direct neighbour of the IPixel whose horizontal energy we wish to
  // calculate.
  public abstract double findHEComponent();

  // This method sets the given IPixels up, down, left and right as the neighbouring IPixels of
  // this APixel; its up-, down-, left- and right- neighbours respectively.
  // EFFECT: Sets the neighbors of this APixel to the given APixels
  public abstract void makeConnectionsPixel(APixel up, APixel down, APixel left, APixel right);

  // This method checks the invariant property of this APixel and its neighbours: that the diagonal
  // neighbours of this APixel are the respective neighbours of this APixel's neighbours.
  // Ex - this APixel's top-neighbour's left-neighbour is the same as this APixel's
  // left-neighbour's top-neighbour.
  public boolean isWellFormed() {
    return (this.up.right == this.right.up
            && this.up.left == this.left.up
            && this.down.left == this.left.down
            && this.down.right == this.right.down);
  }

  // EFFECT: reconnects a pixel that was part of a seam that was removed
  public void reconnectPixel() {
    this.colDisplayed = this.col;
    this.right.left = this;
    this.left.right = this;
    this.down.up = this;
    this.up.down = this;
  }


  // This method computes the updated energies of this APixel in the case that any of its
  // neighbours have been updated.
  // EFFECT: updates the energies of this APixel
  public abstract void updateEnergies();

  // finds the start of the image in the given grid
  public APixel findImageStart() {
    return this.right.down;
  }

  // Confirms whether or not the APixel has a valid down-neighbour.
  public boolean hasDown() {
    return this.down != this;
  }

  // Confirms whether or not the APixel has a valid right-neighbour.
  public boolean hasRight() {
    return this.right != this;
  }

  // Finds and returns the up neighbor of this pixel
  public APixel upNeighbor() {
    return this.up;
  }

  // Finds and returns the down neighbor of this pixel
  public APixel downNeighbor() {
    return this.down;
  }

  // Finds and returns the left neighbor of this pixel
  public APixel leftNeighbor() {
    return this.left;
  }

  // Finds and returns the right neighbor of this pixel
  public APixel rightNeighbor() {
    return this.right;
  }

  // Finds and returns the color displayed for this pixel
  public Color checkColorDisplayed() {
    return this.colDisplayed;
  }

  // EFFECT: changes the colDisplayed field of this pixel
  public void makeColorDisplayedTo(Color col) {
    this.colDisplayed = col;
  }
}

// This represents a colored pixel that is part of the original image.
class Pixel extends APixel {
  double verticalEnergy;
  double horizontalEnergy;
  double energy;

  // Convenience constructor
  Pixel(Color col) {
    super(col);
    this.up = new Border();
    this.down = new Border();
    this.left = new Border();
    this.right = new Border();
    this.verticalEnergy = 0;
    this.horizontalEnergy = 0;
    this.energy = 0;
  }

  // Constructor
  Pixel(Color col, APixel up, APixel down, APixel left, APixel right) {
    super(col, up, down, left, right);
    this.verticalEnergy = 0;
    this.horizontalEnergy = 0;
    this.energy = 0;
  }

  // This method uses this Pixel's horizontal and vertical energies to compute this Pixel's
  // energy.
  public double calculateEnergy() {
    this.updateEnergies();
    return Math.sqrt(Math.pow(this.verticalEnergy, 2) + Math.pow(this.horizontalEnergy, 2));
  }

  // This method computes the vertical energy component of an IPixel on one side, given that
  // this Pixel is the direct neighbour of the IPixel whose vertical energy we wish to calculate.
  public double findVEComponent() {
    return (2 * this.calculateBrightness()) + this.left.calculateBrightness()
            + this.right.calculateBrightness();
  }

  // This method computes the horizontal energy component of an IPixel on one side, given that
  // this Pixel is the direct neighbour of the IPixel whose horizontal energy we wish to
  // calculate.
  public double findHEComponent() {
    return (2 * this.calculateBrightness()) + this.up.calculateBrightness()
            + this.down.calculateBrightness();
  }

  // This method sets the given APixels up, down, left and right as the neighbouring APixels of
  // this Pixel; its up-, down-, left- and right- neighbours respectively.
  // EFFECT: sets the neighbors of this Pixel to the given APixels
  public void makeConnectionsPixel(APixel up, APixel down, APixel left, APixel right) {
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
    up.down = this;
    down.up = this;
    left.right = this;
    right.left = this;
    this.verticalEnergy = this.up.findVEComponent() - this.down.findVEComponent();
    this.horizontalEnergy = this.left.findHEComponent() - this.right.findHEComponent();
    this.energy = this.calculateEnergy();
  }

  // This method computes the updated energies of this Pixel in the case that any of its
  // neighbours have been updated.
  // EFFECT: updates the energies of this Pixel using the given formula
  public void updateEnergies() {
    this.verticalEnergy = this.up.findVEComponent() - this.down.findVEComponent();
    this.horizontalEnergy = this.left.findHEComponent() - this.right.findHEComponent();
    //this.energy = this.calculateEnergy();
  }
}

// This represents a black-colored pixel that is not part of the original image, but is used to
// surround the other Pixels that are.
class Border extends APixel {
  // Constructor
  Border() {
    super(Color.BLACK);
    this.up = this;
    this.down = this;
    this.left = this;
    this.right = this;
  }

  // This method computes the vertical energy component of an IPixel on one side, given that
  // this Border is the direct neighbour of the IPixel whose vertical energy we wish to calculate.
  // Since this is a Border, the vertical energy component of the IPixel will be 0.0.
  public double findVEComponent() {
    return 0.0;
  }

  // This method computes the horizontal energy component of an IPixel on one side, given that
  // this APixel is the direct neighbour of the IPixel whose horizontal energy we wish to
  // calculate.
  // Since this is a Border, the horizontal energy component of the IPixel will be 0.0.
  public double findHEComponent() {
    return 0.0;
  }

  // This method sets the given APixels up, down, left and right as the neighbouring IPixels of
  // this Border; Since the Border is a generic representation of any Border-type IPixel, there
  // is no need to set direct references to the given IPixels.
  // EFFECT: sets the neighbors of this Border to the given APixels
  public void makeConnectionsPixel(APixel up, APixel down, APixel left, APixel right) {
    up.down = this;
    down.up = this;
    left.right = this;
    right.left = this;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
  }

  // This method uses computes this Border's energy.
  // returns the max value to ensure that a Border is never selected for finding the
  // minimum energy.
  public double calculateEnergy() {
    return Integer.MAX_VALUE;
  }

  // This method computes the updated energies of this APixel in the case that any of its
  // neighbours have been updated.
  // EFFECT: updates the energies of this Border -- does nothing because the energy is always
  // Integer.MAX_VALUE
  public void updateEnergies() {
    // does nothing because the energy is always Integer.MAX_VALUE
  }
}

// This represents a seam (a line of pixels traced from one end of the image to an IPixel)
// corresponding to an IPixel in the image, the cumulative, minimum total energy to reach that
// point in the seam, and where the rest of the seam came from, before this IPixel.
class SeamInfo {
  APixel correspondingInfo;
  double totalWeight;
  SeamInfo cameFrom;

  // Convenience Constructor
  SeamInfo(APixel correspondingInfo, double totalWeight) {
    this.correspondingInfo = correspondingInfo;
    this.totalWeight = totalWeight;
    this.cameFrom = null;
  }

  // Constructor
  SeamInfo(APixel correspondingInfo, double totalWeight, SeamInfo cameFrom) {
    this.correspondingInfo = correspondingInfo;
    this.totalWeight = totalWeight;
    this.cameFrom = cameFrom;
  }

  // Finds and returns the totalWeight of this SeamInfo
  double weightSoFar() {
    return this.totalWeight;
  }

  // Finds and returns the correspondingInfo of this SeamInfo
  APixel findMyPixel() {
    return this.correspondingInfo;
  }

  // Finds and returns the cameFrom of this SeamInfo
  SeamInfo prevSeamInfo() {
    return this.cameFrom;
  }

  // checks if this SeamInfo has a cameFrom
  boolean hasCameFrom() {
    return this.cameFrom != null;
  }
}

// This represents the original image that has been chosen to be compressed (seam carved).
class SeamCarver extends World {
  FromFileImage originalImage;
  boolean paused;
  boolean paintRed;
  ComputedPixelImage inColor;
  ComputedPixelImage blackAndWhite;
  ComputedPixelImage displayEnergies;
  APixel topLeft;
  ArrayList<ArrayList<APixel>> pixelHolder;
  ArrayList<SeamInfo> lastRow;
  ArrayList<SeamInfo> lastCol;
  ArrayList<SeamInfo> removed;
  boolean vCarve;
  boolean hCarve;

  // true means a vertical seam was just carved and false means a horizontal seam was just carved
  boolean lastCarved;
  boolean energySnapShot;
  boolean showWeights;
  boolean undoMode;

  // Constructor
  SeamCarver(FromFileImage originalImage) {
    this.originalImage = originalImage;
    this.paused = true;
    this.paintRed = true;
    this.inColor = new ComputedPixelImage((int) this.originalImage.getWidth(),
            (int) this.originalImage.getHeight());
    this.blackAndWhite = new ComputedPixelImage((int) this.originalImage.getWidth(),
            (int) this.originalImage.getHeight());
    this.displayEnergies = new ComputedPixelImage((int) this.originalImage.getWidth(),
            (int) this.originalImage.getHeight());
    this.topLeft = new Border();
    this.lastRow = new ArrayList<>();
    this.lastCol = new ArrayList<>();
    this.removed = new ArrayList<>();
    this.pixelHolder = new ArrayList<>();
    this.vCarve = false;
    this.hCarve = false;
    this.lastCarved = false;
    this.energySnapShot = false;
    this.showWeights = false;
    this.undoMode = false;
    this.makeConnections();
    this.checkIfWellFormed();
    this.seamCarveVertical();
    this.seamCarveHorizontal();
  }

  // This method draws this image in color, on a background and displays it, which is what is
  // seen by the user.
  public WorldScene makeScene() {
    WorldScene res =  new WorldScene((int) this.originalImage.getWidth(),
            (int) this.originalImage.getHeight());
    if (this.energySnapShot) {
      res.placeImageXY(this.blackAndWhite, (int) this.originalImage.getWidth() / 2,
              (int)this.originalImage.getHeight() / 2);
    } else if (this.showWeights) {
      res.placeImageXY(this.displayEnergies, (int) this.originalImage.getWidth() / 2,
              (int)this.originalImage.getHeight() / 2);
    } else {
      res.placeImageXY(this.inColor, (int) this.originalImage.getWidth() / 2,
              (int)this.originalImage.getHeight() / 2);
    }
    return res;
  }

  // This method responds to a key event, wherein the user has pressed a key on the keyboard
  // (which is the given key).
  // If the user presses the space bar, wherein the key is " ", then it will start or stop the
  // seam carving of this image. If the user presses "v" then vertical carving occurs and if they
  // press "h" then horizontal carving occurs. If the user presses "e" then the energies are shown
  // and if the user presses "w" then the total weights are shown.
  // EFFECT: responds to a given key event, with specific actions explained above
  public void onKeyEvent(String key) {
    // to make sure none of the red lines are kept when switching between modes
    if (!(this.paintRed) && this.lastCarved) {
      // if this.lastCarved is true, the last carved thing was vertical
      this.seamRemoveVertical();
    } else if (!(this.paintRed)) {
      this.seamRemoveHorizontal();
    }
    if (key.equals(" ")) {
      this.seamCarveVertical();
      this.seamCarveHorizontal();
      this.paused = !this.paused;
      if (this.paused) {
        // Once the carving pauses, when it starts again, it should reset to randomized carving.
        this.vCarve = false;
        this.hCarve = false;
      }
    } else if (key.equals("v")) {
      this.vCarve = true;
      this.hCarve = false;
    } else if (key.equals("h")) {
      this.vCarve = false;
      this.hCarve = true;
    } else if (key.equals("e")) {
      this.energySnapShot = !this.energySnapShot;
      this.showWeights = false;
    } else if (key.equals("w")) {
      this.showWeights = !this.showWeights;
      this.energySnapShot = false;
    } else if (key.equals("u")) {
      this.undoMode = !this.undoMode;
    }
  }

  // This method is invoked at every tick after the big bang method is invoked.
  // EFFECT: makes all of the necessary connections and makes the world scene on every tick
  // after the start of the big bang method
  public void onTick() {
    if (this.energySnapShot) {
      this.makeBandW();
    } else if (this.showWeights) {
      this.displayCumulativeWeight();
    } else {
      this.makeUpdatedImageColor();
    }

    if (this.paintRed && !this.undoMode) {
      if (this.vCarve || this.hCarve) {
        if (this.vCarve) {
          this.paintVerticalSeamRed();
        } else {
          this.paintHorizontalSeamRed();
        }
      } else if (Math.random() > 0.5) {
        this.paintVerticalSeamRed();
      } else {
        this.paintHorizontalSeamRed();
      }
    } else if (this.undoMode) {
      this.seamCarveVertical();
      this.seamCarveHorizontal();
      this.undoMove();
      this.seamCarveVertical();
      this.seamCarveHorizontal();
    } else {
      if (this.lastCarved) {
        this.seamRemoveVertical();
        this.seamCarveVertical();
        this.seamCarveHorizontal();
      } else {
        this.seamRemoveHorizontal();
        this.seamCarveHorizontal();
        this.seamCarveVertical();
      }
    }
    this.paintRed = !this.paintRed;
    this.checkIfWellFormed();
    this.makeScene();
  }

  //EFFECT: checks if the current grid is well-formed (pixel.up.left = pixel.left.up etc.
  // for all pixels)
  void checkIfWellFormed() {
    APixel start = this.topLeft.findImageStart();
    APixel cur = this.topLeft.findImageStart();

    // LOOP: goes through the rows in the grid to check for well-formedness
    while (start.down.hasDown()) {
      // LOOP: goes through each pixel in each row and checks if that pixel is well-formed
      while (cur.hasRight()) {
        if (!cur.isWellFormed()) {
          throw new RuntimeException("grid is not well formed");
        }
        cur = cur.rightNeighbor();
      }
      cur = start.downNeighbor();
      start = start.downNeighbor();
    }
  }

  // EFFECT: connects all of the pixels initially based on the originalImage
  void makeConnections() {
    ArrayList<APixel> topRow = new ArrayList<>();
    ArrayList<APixel> botRow = new ArrayList<>();
    // Loop statement: loops through the width of the original image and sets a top
    // row of borders
    for (int col = 0; col < this.originalImage.getWidth() + 2; col += 1) {
      topRow.add(new Border());
      botRow.add(new Border());
    }
    this.pixelHolder.add(topRow);

    // Loop statement: loops through the body of the image and adds those APixels to the arraylist
    for (int row = 0; row < this.originalImage.getHeight(); row += 1) {
      ArrayList<APixel> midRow = new ArrayList<>();
      midRow.add(new Border());
      // Loop statement: loops through the columns in this image and adds those APixels
      // to the arraylist
      for (int col = 0; col < this.originalImage.getWidth(); col += 1) {
        Color newColor = this.originalImage.getColorAt(col, row);
        APixel pix = new Pixel(newColor);
        midRow.add(pix);
      }
      midRow.add(new Border());
      this.pixelHolder.add(midRow);
    }
    this.pixelHolder.add(botRow);

    // LOOP: goes through the top row of the ArrayList and makes those connections
    for (int col = 0; col < this.pixelHolder.get(0).size() - 1; col += 1) {
      APixel down = this.pixelHolder.get(1).get(col);
      APixel right = this.pixelHolder.get(0).get(col + 1);
      APixel cur = this.pixelHolder.get(0).get(col);
      cur.makeConnectionsPixel(cur.upNeighbor(), down, cur.leftNeighbor(), right);
      cur.updateEnergies();
    }
    APixel down = this.pixelHolder.get(1).get(this.pixelHolder.get(0).size() - 1);
    APixel cur = this.pixelHolder.get(0).get(this.pixelHolder.get(0).size() - 1);
    cur.makeConnectionsPixel(cur.upNeighbor(), down, cur.leftNeighbor(), cur.rightNeighbor());
    cur.updateEnergies();

    // LOOP: goes through the middle rows of the ArrayList and makes those connections
    for (int row = 1; row < this.pixelHolder.size() - 1; row += 1) {
      // LOOP: goes through the columns of the middle rows of the ArrayList
      // and makes those connections
      for (int col = 0; col < this.pixelHolder.get(0).size() - 1; col += 1) {
        APixel down1 = this.pixelHolder.get(row + 1).get(col);
        APixel right = this.pixelHolder.get(row).get(col + 1);
        APixel cur1 = this.pixelHolder.get(row).get(col);
        cur1.makeConnectionsPixel(cur1.upNeighbor(), down1, cur1.leftNeighbor(), right);
        cur1.updateEnergies();
      }
      APixel d = this.pixelHolder.get(row + 1).get(this.pixelHolder.get(0).size() - 1);
      APixel c = this.pixelHolder.get(row).get(this.pixelHolder.get(0).size() - 1);
      c.makeConnectionsPixel(c.upNeighbor(), d, c.leftNeighbor(), c.rightNeighbor());
      c.updateEnergies();
    }

    // LOOP: goes through the bottom row of the ArrayList and makes those connections
    for (int col = 0; col < this.pixelHolder.get(0).size() - 1; col += 1) {
      APixel right = this.pixelHolder.get(this.pixelHolder.size() - 1).get(col + 1);
      APixel cur2 = this.pixelHolder.get(this.pixelHolder.size() - 1).get(col);
      cur2.makeConnectionsPixel(cur2.upNeighbor(), cur2.downNeighbor(),
              cur2.leftNeighbor(), right);
      cur2.updateEnergies();
    }
    this.topLeft = this.pixelHolder.get(0).get(0);
  }

  // EFFECT: makes an updated version of this SeamCarver in color
  public void makeUpdatedImageColor() {
    this.inColor.setPixels(0, 0, (int) this.originalImage.getWidth(),
            (int) this.originalImage.getHeight(), Color.WHITE);

    int x = 0;
    int y = 0;
    APixel curRowCounter = this.topLeft.findImageStart();
    // LOOP: goes through the rows in the grid to set colors of pixels in this.inColor in each row
    while (curRowCounter.hasDown()) {
      APixel curColCounter = curRowCounter;
      x = 0;
      // LOOP: goes through the pixels in a given row to set the color in this.inColor to
      // correspond to the color of the pixel
      while (curColCounter.hasRight()) {
        this.inColor.setColorAt(x, y, curColCounter.checkColorDisplayed());
        x += 1;
        curColCounter = curColCounter.rightNeighbor();
      }
      y += 1;
      curRowCounter = curRowCounter.downNeighbor();
    }
  }

  // EFFECT: makes an updated version of this SeamCarver that displays the energies of each pixel
  public void makeBandW() {
    this.blackAndWhite.setPixels(0, 0, (int) this.originalImage.getWidth(),
            (int) this.originalImage.getHeight(), Color.WHITE);

    int x = 0;
    int y = 0;
    APixel curRowCounter = this.topLeft.findImageStart();

    double max = Math.sqrt(32.0);
    // LOOP: goes through the rows in the grid to set colors of
    // pixels in this.blackAndWhite in each row
    while (curRowCounter.hasDown()) {
      APixel curColCounter = curRowCounter;
      x = 0;
      // LOOP: goes through the pixels in a given row to set the color in this.blackAndWhite to
      // correspond to the color of the pixel
      while (curColCounter.hasRight()) {
        int energyFrac = (int) ((curColCounter.calculateEnergy() / max) * 255.0);
        this.blackAndWhite.setColorAt(x, y, new Color(energyFrac, energyFrac, energyFrac));
        x += 1;
        curColCounter = curColCounter.rightNeighbor();
      }
      y += 1;
      curRowCounter = curRowCounter.downNeighbor();
    }
  }

  // EFFECT: makes an updated version of this SeamCarver that displays the cumulative weights
  // of each pixel
  public void displayCumulativeWeight() {
    this.displayEnergies.setPixels(0, 0, (int) this.originalImage.getWidth(),
            (int) this.originalImage.getHeight(), Color.WHITE);

    double maxEnergy = 0.0;
    this.lastRow = new ArrayList<SeamInfo>();
    ArrayList<SeamInfo> first = new ArrayList<SeamInfo>();
    APixel curRowCounter = this.topLeft.findImageStart();
    // LOOP: goes through the first row of pixels and finds the SeamInfo corresponding to each
    // pixel and adds that SeamInfo to the ArrayList "first"
    while (curRowCounter.hasRight()) {
      SeamInfo newSeam = new SeamInfo(curRowCounter, curRowCounter.calculateEnergy());
      first.add(newSeam);
      curRowCounter = curRowCounter.rightNeighbor();
    }

    curRowCounter = this.topLeft.findImageStart().downNeighbor();
    APixel curColCounter = curRowCounter;
    int counter = 0;
    // LOOP: goes through each row of pixels to calculate the SeamInfo of pixels in these rows
    while (curRowCounter.hasDown()) {
      curColCounter = curRowCounter;
      counter = 0;
      ArrayList<SeamInfo> nextRow = new ArrayList<SeamInfo>();
      // LOOP: goes through each pixel in a given row to calculate the SeamInfo of those pixels
      // and add those SeamInfos to the "nextRow" ArrayList
      // also checks if the energy of the pixel is greater than the current maxEnergy
      while (curColCounter.hasRight()) {
        SeamInfo newSeam = new SeamInfo(curColCounter,
                this.findMinimumSeam(first, counter).weightSoFar()
                        + curColCounter.calculateEnergy(), this.findMinimumSeam(first, counter));
        nextRow.add(newSeam);
        if (newSeam.weightSoFar() > maxEnergy) {
          maxEnergy = newSeam.weightSoFar();
        }
        curColCounter = curColCounter.rightNeighbor();
        counter += 1;
      }
      first = nextRow;
      curRowCounter = curRowCounter.downNeighbor();
    }
    this.lastRow = first;

    curRowCounter = this.topLeft.findImageStart().downNeighbor();
    first = new ArrayList<>();
    // LOOP: goes through to calculate the SeamInfos of the pixels in the first row to help with
    // later calculations
    while (curRowCounter.hasRight()) {
      SeamInfo newSeam = new SeamInfo(curRowCounter, curRowCounter.calculateEnergy());
      first.add(newSeam);
      curRowCounter = curRowCounter.rightNeighbor();
    }

    // LOOP: goes through each location of a SeamInfo in the "first" ArrayList and sets the
    // color at the given location based on the relative value of the totalWeight of the SeamInfo
    // at the given location
    for (int col = 0; col < first.size(); col += 1) {
      int newVal = (int) ((first.get(col).weightSoFar() / maxEnergy) * 255.0);
      this.displayEnergies.setColorAt(col, 0, new Color(newVal, newVal, newVal));
    }

    curRowCounter = this.topLeft.findImageStart().downNeighbor();
    curColCounter = curRowCounter;
    counter = 0;
    int rowCounter = 1;
    // LOOP: goes through the rows in the grid to set the colors at those locations
    while (curRowCounter.hasDown()) {
      curColCounter = curRowCounter;
      counter = 0;
      ArrayList<SeamInfo> nextRow = new ArrayList<SeamInfo>();
      // LOOP: goes through each pixel in a given row and adds the SeamInfo with that pixel's
      // information to "nextRow" to help with color setting
      while (curColCounter.hasRight()) {
        nextRow.add(new SeamInfo(curColCounter,
                this.findMinimumSeam(first, counter).weightSoFar()
                        + curColCounter.calculateEnergy(), this.findMinimumSeam(first, counter)));
        curColCounter = curColCounter.rightNeighbor();
        counter += 1;
      }
      first = nextRow;

      // LOOP: goes through each index of "first" and sets the color at the corresponding
      // location in this.displayEnergies to reflect the relative totalWeight of the
      // SeamInfo at the given index
      for (int col = 0; col < first.size(); col += 1) {
        int newVal = (int) ((first.get(col).weightSoFar() / maxEnergy) * 255.0);
        this.displayEnergies.setColorAt(col, rowCounter, new Color(newVal, newVal, newVal));
      }
      curRowCounter = curRowCounter.downNeighbor();
      rowCounter += 1;
    }
  }

  // EFFECT: This method populates the last row of SeamInfos once the pixels are fully traversed.
  public void seamCarveVertical() {
    this.lastRow = new ArrayList<SeamInfo>();
    ArrayList<SeamInfo> first = new ArrayList<SeamInfo>();
    APixel curRowCounter = this.topLeft.findImageStart();
    // LOOP: goes through each element of the first row and adds a SeamInfo to "first"
    // corresponding to each element of that row
    while (curRowCounter.hasRight()) {
      first.add(new SeamInfo(curRowCounter, curRowCounter.calculateEnergy()));
      curRowCounter = curRowCounter.rightNeighbor();
    }

    curRowCounter = this.topLeft.findImageStart().downNeighbor();
    APixel curColCounter = curRowCounter;
    int counter = 0;
    // LOOP: goes through each row of the grid to calculate SeamInfos
    while (curRowCounter.hasDown()) {
      curColCounter = curRowCounter;
      counter = 0;
      ArrayList<SeamInfo> nextRow = new ArrayList<SeamInfo>();
      // LOOP: goes through each element of a given row and adds a SeamInfo to "nextRow"
      // corresponding to each element
      while (curColCounter.hasRight()) {
        nextRow.add(new SeamInfo(curColCounter,
                this.findMinimumSeam(first, counter).weightSoFar()
                        + curColCounter.calculateEnergy(), this.findMinimumSeam(first, counter)));
        curColCounter = curColCounter.rightNeighbor();
        counter += 1;
      }
      first = nextRow;
      curRowCounter = curRowCounter.downNeighbor();
    }
    this.lastRow = first;
  }

  // EFFECT: This method populates the last col of SeamInfos once the pixels are fully traversed
  public void seamCarveHorizontal() {
    this.lastCol = new ArrayList<SeamInfo>();
    ArrayList<SeamInfo> first = new ArrayList<SeamInfo>();
    APixel curColCounter = this.topLeft.findImageStart();
    // LOOP: goes through each element of the first column and adds a SeamInfo to "first"
    // corresponding to each element of that column
    while (curColCounter.hasDown()) {
      first.add(new SeamInfo(curColCounter, curColCounter.calculateEnergy()));
      curColCounter = curColCounter.downNeighbor();
    }

    curColCounter = this.topLeft.findImageStart().rightNeighbor();
    APixel curRowCounter = curColCounter;
    int counter = 0;
    // LOOP: goes through each column of the grid to calculate SeamInfos
    while (curColCounter.hasRight()) {
      curRowCounter = curColCounter;
      counter = 0;
      ArrayList<SeamInfo> nextRow = new ArrayList<SeamInfo>();
      // LOOP: goes through each element in a given column and adds a SeamInfo to "nextRow"
      // corresponding to each element
      while (curRowCounter.hasDown()) {
        nextRow.add(new SeamInfo(curRowCounter,
                this.findMinimumSeam(first, counter).weightSoFar()
                        + curRowCounter.calculateEnergy(), this.findMinimumSeam(first, counter)));
        curRowCounter = curRowCounter.downNeighbor();
        counter += 1;
      }
      curColCounter = curColCounter.rightNeighbor();
      first = nextRow;
    }
    this.lastCol = first;
  }

  // This method inspects the SeamInfo of the pixel at the given row and col, and its horizontal
  // neighbours. Of the three SeamInfos examined, this method will help find the SeamInfo with the
  // minimum total weight so far, and return that SeamInfo. If any of the SeamInfos' pixels
  // correspond to a Border, that is not considered.
  SeamInfo findMinimumSeam(ArrayList<SeamInfo> prevRow, int col) {
    SeamInfo left = new SeamInfo(new Border(), Integer.MAX_VALUE);
    // set as the max value for an Integer so that this value is never used
    SeamInfo right = new SeamInfo(new Border(), Integer.MAX_VALUE);
    // set as the max value for an Integer so that this value is never used
    if (col > 0) {
      left = prevRow.get(col - 1);
    }
    SeamInfo above = prevRow.get(col);
    if (col < prevRow.size() - 1) {
      right = prevRow.get(col + 1);
    }
    if (left.weightSoFar() <= above.weightSoFar()
            && (left.weightSoFar() <= right.weightSoFar())) {
      return left;
    } else if (above.weightSoFar() <= right.weightSoFar()
            && (above.weightSoFar() <= left.weightSoFar())) {
      return above;
    } else {
      return right;
    }
  }

  // EFFECT: sets all of the colDisplayed fields of the pixels along the min weight path to be red
  // for vertical
  public void paintVerticalSeamRed() {
    if (!this.paused && (this.lastRow.size() > 0)) {
      SeamInfo min = this.lastRow.get(0);
      // LOOP: This loop iterates through every SeamInfo in the last row of the infoGrid, and
      // keeps the SeamInfo with the minimum totalWeight.
      for (SeamInfo s : this.lastRow) {
        if (s.weightSoFar() < min.weightSoFar()) {
          min = s;
        }
      }

      // LOOP: goes through as long as min.cameFrom is not null and paints the pixels
      // red along the minimum seam
      while (min.prevSeamInfo() != null) {
        min.findMyPixel().makeColorDisplayedTo(Color.RED);
        min = min.prevSeamInfo();
      }
      this.lastCarved = true;
    }
  }

  // EFFECT: sets all of the colDisplayed fields of the pixels along the min weight path to be red
  // for horizontal
  public void paintHorizontalSeamRed() {
    if (!this.paused && (this.lastCol.size() > 0)) {
      SeamInfo min = this.lastCol.get(0);
      // LOOP: This loop iterates through every SeamInfo in the last row of the infoGrid, and
      // keeps the SeamInfo with the minimum totalWeight.
      for (SeamInfo s : this.lastCol) {
        if (s.weightSoFar() < min.weightSoFar()) {
          min = s;
        }
      }

      // LOOP: goes through as long as min.cameFrom is not null and paints the pixels
      // red along the minimum seam
      while (min.prevSeamInfo() != null) {
        min.findMyPixel().makeColorDisplayedTo(Color.RED);
        min = min.prevSeamInfo();
      }
      this.lastCarved = false;
    }
  }

  // EFFECT: removes the minimum weighted seam in the ArrayList of SeamInfos and
  // removes the Pixels in that seam by changing the connections of those pixels.
  // for vertical
  public void seamRemoveVertical() {
    if (!this.paused && (this.lastRow.size() > 0)) {
      SeamInfo min = this.lastRow.get(0);
      // LOOP: This loop iterates through every SeamInfo in the last row of the infoGrid, and
      // keeps the SeamInfo with the minimum totalWeight.
      for (SeamInfo s : this.lastRow) {
        if (s.weightSoFar() < min.weightSoFar()) {
          min = s;
        }
      }
      this.removed.add(0, min);

      APixel pix = min.findMyPixel().downNeighbor().rightNeighbor();
      pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
              min.findMyPixel().downNeighbor().leftNeighbor(), pix.rightNeighbor());

      pix = min.findMyPixel().downNeighbor().leftNeighbor();
      pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(), pix.leftNeighbor(),
              min.findMyPixel().downNeighbor().rightNeighbor());

      // LOOP: goes through as long as min.cameFrom is not null and makes sure that
      // the connections are fixed when the minimum seam is removed
      while (min.hasCameFrom()) {
        if (min.prevSeamInfo().findMyPixel() == min.findMyPixel().upNeighbor().leftNeighbor()) {
          pix = min.findMyPixel().upNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), min.findMyPixel().leftNeighbor(),
                  pix.leftNeighbor(), pix.rightNeighbor());

          pix = min.findMyPixel().leftNeighbor();
          pix.makeConnectionsPixel(min.findMyPixel().upNeighbor(), pix.downNeighbor(),
                  pix.leftNeighbor(), pix.rightNeighbor());
        } else if (min.prevSeamInfo().findMyPixel()
                == min.findMyPixel().upNeighbor().rightNeighbor()) {
          pix = min.findMyPixel().upNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), min.findMyPixel().rightNeighbor(),
                  pix.leftNeighbor(), pix.rightNeighbor());

          pix = min.findMyPixel().rightNeighbor();
          pix.makeConnectionsPixel(min.findMyPixel().upNeighbor(), pix.downNeighbor(),
                  pix.leftNeighbor(), pix.rightNeighbor());
        }

        pix = min.findMyPixel().leftNeighbor();
        pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(), pix.leftNeighbor(),
                min.findMyPixel().rightNeighbor());

        min = min.prevSeamInfo();
      }
      pix = min.findMyPixel().rightNeighbor();
      pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
              min.findMyPixel().leftNeighbor(), pix.rightNeighbor());

      pix = min.findMyPixel().upNeighbor().rightNeighbor();
      pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
              min.findMyPixel().upNeighbor().leftNeighbor(), pix.rightNeighbor());

      min.findMyPixel().leftNeighbor().updateEnergies();
      min.findMyPixel().rightNeighbor().updateEnergies();
      this.lastCarved = true;
    }
  }

  // EFFECT: removes the minimum weighted seam in the ArrayList of SeamInfos and
  // removes the Pixels in that seam by changing the connections of those pixels.
  // for horizontal
  public void seamRemoveHorizontal() {
    if (!this.paused && (this.lastCol.size() > 1)) {
      SeamInfo min = this.lastCol.get(0);
      // LOOP: This loop iterates through every SeamInfo in the last row of the infoGrid, and
      // keeps the SeamInfo with the minimum totalWeight.
      for (SeamInfo s : this.lastCol) {
        if (s.weightSoFar() < min.weightSoFar()) {
          min = s;
        }
      }

      this.removed.add(0, min);

      APixel pix = min.findMyPixel().rightNeighbor().upNeighbor();
      pix.makeConnectionsPixel(pix.upNeighbor(), min.findMyPixel().rightNeighbor().downNeighbor(),
              pix.leftNeighbor(), pix.rightNeighbor());

      pix = min.findMyPixel().rightNeighbor().downNeighbor();
      pix.makeConnectionsPixel(min.findMyPixel().rightNeighbor().upNeighbor(), pix.downNeighbor(),
              pix.leftNeighbor(), pix.rightNeighbor());

      // LOOP: goes through as long as min.cameFrom is not null and makes sure that
      // the connections are fixed when the minimum seam is removed
      while (min.hasCameFrom()) {
        if (min.prevSeamInfo().findMyPixel() == min.findMyPixel().leftNeighbor().upNeighbor()) {
          pix = min.findMyPixel().upNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
                  min.findMyPixel().leftNeighbor(), pix.rightNeighbor());

          pix = min.findMyPixel().leftNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(), pix.leftNeighbor(),
                  min.findMyPixel().upNeighbor());
        } else if (min.prevSeamInfo().findMyPixel()
                == min.findMyPixel().leftNeighbor().downNeighbor()) {
          pix = min.findMyPixel().downNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
                  min.findMyPixel().leftNeighbor(), pix.rightNeighbor());

          pix = min.findMyPixel().leftNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
                  pix.leftNeighbor(), min.findMyPixel().downNeighbor());
        }

        pix = min.findMyPixel().upNeighbor();
        pix.makeConnectionsPixel(pix.upNeighbor(), min.findMyPixel().downNeighbor(),
                pix.leftNeighbor(), pix.rightNeighbor());

        min = min.prevSeamInfo();
      }

      pix = min.findMyPixel().upNeighbor();
      pix.makeConnectionsPixel(pix.upNeighbor(), min.findMyPixel().downNeighbor(),
              pix.leftNeighbor(), pix.rightNeighbor());

      pix = min.findMyPixel().leftNeighbor().upNeighbor();
      pix.makeConnectionsPixel(pix.upNeighbor(), min.findMyPixel().leftNeighbor().downNeighbor(),
              pix.leftNeighbor(), pix.rightNeighbor());

      min.findMyPixel().upNeighbor().updateEnergies();
      min.findMyPixel().downNeighbor().updateEnergies();
      this.lastCarved = false;
    }
  }

  // EFFECT: adds the last removed seam to the grid and reconnects the pixels properly
  void undoMove() {
    if (!this.paused) {
      if (this.removed.size() != 0) {
        SeamInfo lastRemoved = this.removed.remove(0);
        boolean wasVertical = false;
        lastRemoved.findMyPixel().reconnectPixel();
        if (!lastRemoved.findMyPixel().downNeighbor().hasDown()
                && lastRemoved.findMyPixel().rightNeighbor().hasRight()) {
          wasVertical = true;
        }
        if (wasVertical) {
          APixel pix = lastRemoved.findMyPixel().downNeighbor().rightNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
                  lastRemoved.findMyPixel().downNeighbor(), pix.rightNeighbor());

          pix = lastRemoved.findMyPixel().downNeighbor().leftNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
                  pix.leftNeighbor(), lastRemoved.findMyPixel().downNeighbor());
        } else {
          APixel pix = lastRemoved.findMyPixel().rightNeighbor().downNeighbor();
          pix.makeConnectionsPixel(lastRemoved.findMyPixel().rightNeighbor(),
                  pix.downNeighbor(), pix.leftNeighbor(), pix.rightNeighbor());

          pix = lastRemoved.findMyPixel().rightNeighbor().upNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), lastRemoved.findMyPixel().rightNeighbor(),
                  pix.leftNeighbor(), pix.rightNeighbor());


        }
        // LOOP: goes through the pixels in lastRemoved and reconnects those pixels in the grid
        while (lastRemoved.hasCameFrom()) {
          lastRemoved.findMyPixel().reconnectPixel();
          lastRemoved = lastRemoved.prevSeamInfo();
        }
        lastRemoved.findMyPixel().reconnectPixel();
        if (wasVertical) {
          APixel pix = lastRemoved.findMyPixel().upNeighbor().leftNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(), pix.leftNeighbor(),
                  lastRemoved.findMyPixel().upNeighbor());

          pix = lastRemoved.findMyPixel().upNeighbor().rightNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), pix.downNeighbor(),
                  lastRemoved.findMyPixel().upNeighbor(), pix.rightNeighbor());
        } else {
          APixel pix = lastRemoved.findMyPixel().leftNeighbor().upNeighbor();
          pix.makeConnectionsPixel(pix.upNeighbor(), lastRemoved.findMyPixel().leftNeighbor(),
                  pix.leftNeighbor(), pix.rightNeighbor());


          pix = lastRemoved.findMyPixel().leftNeighbor().downNeighbor();
          pix.makeConnectionsPixel(lastRemoved.findMyPixel().leftNeighbor(), pix.downNeighbor(),
                  pix.leftNeighbor(), pix.rightNeighbor());

          pix = lastRemoved.findMyPixel().downNeighbor().leftNeighbor();
          pix.makeConnectionsPixel(lastRemoved.findMyPixel().leftNeighbor(), pix.downNeighbor(),
                  pix.leftNeighbor(), pix.rightNeighbor());
        }
      }
    }
  }
}

class ExamplesSeamCarver {
  void testBigBang(Tester t) {
    SeamCarver im = new SeamCarver(new FromFileImage("images/lerner.jpg"));
    SeamCarver res = im;
    res.bigBang((int) res.originalImage.getWidth(),
            (int) res.originalImage.getHeight(), 0.001);
  }




  void testCalculateBrightness(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Border border = new Border();
    t.checkInexact(pix1.calculateBrightness(), 0.530718954248366, 0.0001);
    t.checkInexact(pix2.calculateBrightness(), 0.4928104575163399, 0.0001);
    t.checkInexact(pix3.calculateBrightness(), 0.550326797385621, 0.0001);
    t.checkInexact(border.calculateBrightness(), 0.0, 0.0001);
  }

  void testFindVEComponent(Tester t) {
    Border border = new Border();
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    t.checkInexact(pix4.findVEComponent(), 1.603921568627451, 0.0001);
    t.checkInexact(pix5.findVEComponent(), 1.231372549019608, 0.0001);
    t.checkInexact(pix6.findVEComponent(), 1.6405228758169934, 0.0001);
    t.checkInexact(border.findVEComponent(), 0.0, 0.0001);
  }

  void testFindHEComponent(Tester t) {
    Border border = new Border();
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    t.checkInexact(pix7.findHEComponent(), 1.580392156862745, 0.0001);
    t.checkInexact(pix8.findHEComponent(), 1.823529411764706, 0.0001);
    t.checkInexact(pix9.findHEComponent(), 1.9032679738562093, 0.0001);
    t.checkInexact(border.findHEComponent(), 0.0, 0.0001);
  }

  void testCalculateEnergy(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    t.checkInexact(pix10.calculateEnergy(), 0.836, 0.01);
    t.checkInexact(pix12.calculateEnergy(), 1.163, 0.01);
    t.checkInexact(pix13.calculateEnergy(), 1.408, 0.01);
    t.checkInexact(pix15.calculateEnergy(), 1.307, 0.01);
    t.checkInexact(new Border().calculateEnergy(), 2.147483647E9, 0.01);
  }

  // tests that the upNeighbor method works as expected
  void testUpNeighbor(Tester t) {
    Border b = new Border();
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    b.down = pix2;
    t.checkExpect(pix10.upNeighbor(), pix6);
    t.checkExpect(pix7.upNeighbor(), pix3);
    t.checkExpect(pix9.upNeighbor(), pix5);
    t.checkExpect(pix2.upNeighbor(), b);
    t.checkExpect(new Border().upNeighbor(), new Border());
  }

  // tests that the downNeighbor method works as expected
  void testDownNeighbor(Tester t) {
    Border b = new Border();
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    b.up = pix16;
    t.checkExpect(pix16.downNeighbor(), b);
    t.checkExpect(pix7.downNeighbor(), pix11);
    t.checkExpect(pix9.downNeighbor(), pix13);
    t.checkExpect(pix2.downNeighbor(), pix6);
  }

  // tests that the leftNeighbor method works as expected
  void testLeftNeighbor(Tester t) {
    Border b = new Border();
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    b.right = pix5;
    t.checkExpect(pix10.leftNeighbor(), pix9);
    t.checkExpect(pix7.leftNeighbor(), pix6);
    t.checkExpect(pix5.leftNeighbor(), b);
    t.checkExpect(pix2.leftNeighbor(), pix1);
  }

  // tests that the rightNeighbor method works as expected
  void testRightNeighbor(Tester t) {
    Border b = new Border();
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    b.left = pix8;
    t.checkExpect(pix10.rightNeighbor(), pix11);
    t.checkExpect(pix7.rightNeighbor(), pix8);
    t.checkExpect(pix8.rightNeighbor(), b);
    t.checkExpect(pix2.rightNeighbor(), pix3);
  }

  // tests that the hasDown method works as expected
  void testHasDown(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    t.checkExpect(pix10.hasDown(), true);
    t.checkExpect(pix7.hasDown(), true);
    t.checkExpect(new Border().hasDown(), false);
    t.checkExpect(pix16.down.hasDown(), false);
  }

  // tests that the hasRight method works as expected
  void testHasRight(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    t.checkExpect(pix4.right.hasRight(), false);
    t.checkExpect(pix7.hasRight(), true);
    t.checkExpect(pix15.hasRight(), true);
    t.checkExpect(new Border().hasRight(), false);
  }

  // tests that findImageStart works as expected
  void testFindImageStart(Tester t) {
    Border b = new Border();
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    b.up = pix15;
    t.checkExpect(pix1.findImageStart(), pix6);
    t.checkExpect(pix14.findImageStart(), b);
    t.checkExpect(pix5.findImageStart(), pix10);
    t.checkExpect(pix10.findImageStart(), pix15);
    t.checkExpect(b.findImageStart(), b);
  }

  // tests that the checkColorDisplayed method works as expected
  void testCheckColorDisplayed(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    t.checkExpect(pix1.checkColorDisplayed(), new Color(98, 132, 176));
    t.checkExpect(pix14.checkColorDisplayed(), new Color(94, 33, 41));
    t.checkExpect(pix5.checkColorDisplayed(), new Color(2, 90, 222));
    t.checkExpect(pix10.checkColorDisplayed(), new Color(1, 93, 82));
    t.checkExpect(new Border().checkColorDisplayed(), Color.BLACK);
  }

  // tests that the makeColDisplayedTo method works as expected
  void testMakeColDisplayedTo(Tester t) {
    Pixel pix14 = new Pixel(new Color(33, 44, 55));
    Pixel pix1 = new Pixel(new Color(22, 33, 44));
    Pixel pix5 = new Pixel(new Color(1, 2, 3));
    Pixel pix10 = new Pixel(new Color(4, 5, 6));
    Border b = new Border();
    b.makeColorDisplayedTo(Color.BLUE);
    pix14.makeColorDisplayedTo(new Color(1, 1, 1));
    pix1.makeColorDisplayedTo(new Color(4, 67, 100));
    pix5.makeColorDisplayedTo(new Color(6, 30, 22));
    pix10.makeColorDisplayedTo(new Color(9, 90, 200));
    t.checkExpect(pix1.colDisplayed, new Color(4, 67, 100));
    t.checkExpect(pix14.colDisplayed, new Color(1, 1, 1));
    t.checkExpect(pix5.colDisplayed, new Color(6, 30, 22));
    t.checkExpect(pix10.colDisplayed, new Color(9, 90, 200));
    t.checkExpect(b.colDisplayed, Color.BLUE);
  }

  // tests that the weightSoFar method works as expected
  void testWeightSoFar(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    SeamInfo s = new SeamInfo(pix1, 12.4);
    SeamInfo s2 = new SeamInfo(pix12, 121.4);
    SeamInfo s3 = new SeamInfo(pix13, 99.4);
    SeamInfo s4 = new SeamInfo(pix5, 33.88);
    t.checkInexact(s.weightSoFar(), 12.4, 0.01);
    t.checkInexact(s2.weightSoFar(), 121.4, 0.01);
    t.checkInexact(s3.weightSoFar(), 99.4, 0.01);
    t.checkInexact(s4.weightSoFar(), 33.88, 0.01);
  }

  // tests that prevSeamInfo works as expected
  void testPrevSeamInfo(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    SeamInfo s = new SeamInfo(pix1, 12.4);
    SeamInfo s2 = new SeamInfo(pix12, 121.4, s);
    SeamInfo s3 = new SeamInfo(pix13, 12, s2);
    SeamInfo s4 = new SeamInfo(pix5, 33.88, s3);
    t.checkExpect(s.prevSeamInfo(), null);
    t.checkExpect(s2.prevSeamInfo(), s);
    t.checkExpect(s3.prevSeamInfo(), s2);
    t.checkExpect(s4.prevSeamInfo(), s3);
  }

  // tests that the findMyPixel works as expected
  void testFindMyPixel(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    SeamInfo s = new SeamInfo(pix1, 12.4);
    SeamInfo s2 = new SeamInfo(pix12, 121.4, s);
    SeamInfo s3 = new SeamInfo(pix13, 12, s2);
    SeamInfo s4 = new SeamInfo(pix5, 33.88, s3);
    t.checkExpect(s.findMyPixel(), pix1);
    t.checkExpect(s2.findMyPixel(), pix12);
    t.checkExpect(s3.findMyPixel(), pix13);
    t.checkExpect(s4.findMyPixel(), pix5);
  }

  // tests that the hasCameFrom method works as expected
  void testHasCameFrom(Tester t) {
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    SeamInfo s = new SeamInfo(pix1, 12.4);
    SeamInfo s2 = new SeamInfo(pix12, 121.4, s);
    SeamInfo s3 = new SeamInfo(pix13, 12, s2);
    SeamInfo s4 = new SeamInfo(pix5, 33.88, s3);
    t.checkExpect(s.hasCameFrom(), false);
    t.checkExpect(s2.hasCameFrom(), true);
    t.checkExpect(s3.hasCameFrom(), true);
    t.checkExpect(s4.hasCameFrom(), true);
  }

  void testMakeInColor(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    ComputedPixelImage isInColor = new ComputedPixelImage(4, 4);
    isInColor.setPixel(0, 0, new Color(98, 132, 176));
    isInColor.setPixel(1, 0, new Color(123, 231, 23));
    isInColor.setPixel(2, 0, new Color(12, 155, 254));
    isInColor.setPixel(3, 0, new Color(5, 197, 201));
    isInColor.setPixel(0, 1, new Color(2, 90, 222));
    isInColor.setPixel(1, 1, new Color(2, 90, 222));
    isInColor.setPixel(2, 1, new Color(2, 90, 221));
    isInColor.setPixel(3, 1, new Color(2, 90, 221));
    isInColor.setPixel(0, 2, new Color(139, 32, 215));
    isInColor.setPixel(1, 2, new Color(1, 93, 82));
    isInColor.setPixel(2, 2, new Color(76, 47, 39));
    isInColor.setPixel(3, 2, new Color(127, 118, 121));
    isInColor.setPixel(0, 3, new Color(125, 132, 113));
    isInColor.setPixel(1, 3, new Color(94, 33, 41));
    isInColor.setPixel(2, 3, new Color(179, 40, 33));
    isInColor.setPixel(3, 3, new Color(180, 76, 67));
    img.makeUpdatedImageColor();
    t.checkExpect(img.inColor, isInColor);
  }

  void testMakeBandW(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    ComputedPixelImage blackAndWhite = new ComputedPixelImage(4, 4);
    blackAndWhite.setPixel(0, 0, new Color(83, 83, 83));
    blackAndWhite.setPixel(1, 0, new Color(73, 73, 73));
    blackAndWhite.setPixel(2, 0, new Color(73, 73, 73));
    blackAndWhite.setPixel(3, 0, new Color(87, 87, 87));
    blackAndWhite.setPixel(0, 1, new Color(71, 71, 71));
    blackAndWhite.setPixel(1, 1, new Color(42, 42, 42));
    blackAndWhite.setPixel(2, 1, new Color(46, 46, 46));
    blackAndWhite.setPixel(3, 1, new Color(73, 73, 73));
    blackAndWhite.setPixel(0, 2, new Color(49, 49, 49));
    blackAndWhite.setPixel(1, 2, new Color(37, 37, 37));
    blackAndWhite.setPixel(2, 2, new Color(34, 34, 34));
    blackAndWhite.setPixel(3, 2, new Color(52, 52, 52));
    blackAndWhite.setPixel(0, 3, new Color(63, 63, 63));
    blackAndWhite.setPixel(1, 3, new Color(59, 59, 59));
    blackAndWhite.setPixel(2, 3, new Color(58, 58, 58));
    blackAndWhite.setPixel(3, 3, new Color(65, 65, 65));
    img.makeBandW();

    t.checkExpect(img.blackAndWhite, blackAndWhite);
  }

  void testDisplayCumulativeWeight(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    ComputedPixelImage weights = new ComputedPixelImage(4, 4);
    weights.setPixel(0, 0, new Color(83, 83, 83));
    weights.setPixel(1, 0, new Color(49, 49, 49));
    weights.setPixel(2, 0, new Color(54, 54, 54));
    weights.setPixel(3, 0, new Color(86, 86, 86));
    weights.setPixel(0, 1, new Color(132, 132, 132));
    weights.setPixel(1, 1, new Color(98, 98, 98));
    weights.setPixel(2, 1, new Color(103, 103, 103));
    weights.setPixel(3, 1, new Color(141, 141, 141));
    weights.setPixel(0, 2, new Color(156, 156, 156));
    weights.setPixel(1, 2, new Color(142, 142, 142));
    weights.setPixel(2, 2, new Color(139, 139, 139));
    weights.setPixel(3, 2, new Color(165, 165, 165));
    weights.setPixel(0, 3, new Color(217, 217, 217));
    weights.setPixel(1, 3, new Color(209, 209, 209));
    weights.setPixel(2, 3, new Color(208, 208, 208));
    weights.setPixel(3, 3, new Color(216, 216, 216));
    img.displayCumulativeWeight();
    t.checkExpect(img.displayEnergies, weights);
  }

  void testIsWellFormed(Tester t) {
    APixel not = new Pixel(Color.BLUE);
    not.down = new Pixel(Color.BLACK);
    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(), pix1,
            new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(), pix2,
            new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(), pix3,
            new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(), new Border(),
            new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    t.checkExpect(pix7.isWellFormed(), true);
    t.checkExpect(not.isWellFormed(), false);
    t.checkExpect(pix10.isWellFormed(), true);
    t.checkExpect(new Border().isWellFormed(), true);
  }

  // tests that checkIfWellFormed works as expected + exception
  void testCheckIfWellFormed(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    img.makeConnections();
    img.checkIfWellFormed();
    // since this is a void method and this method isn't changing anything, we can't
    // write a check expect to compare this to anything
    SeamCarver img2 = new SeamCarver(new FromFileImage("images/lerner.jpg"));
    img2.makeConnections();
    img2.checkIfWellFormed();

    SeamCarver img3 = new SeamCarver(new FromFileImage("images/lerner.jpg"));
    img3.topLeft.down.right = new Pixel(Color.BLACK);
    img3.topLeft.down.right.up = new Pixel(Color.BLUE);
    // in theory, this should throw an exception, but our code is structured in a way
    // such that the exception should never be reached because all of the connections
    // are made properly in the constructor
    img3.checkIfWellFormed();
  }

  void testUpdateEnergies(Tester t) {
    Border b = new Border();
    Border bNotChanged = new Border();
    b.updateEnergies();
    t.checkExpect(b, bNotChanged);

    Pixel pix1 = new Pixel(new Color(98, 132, 176), new Border(), new Border(),
            new Border(), new Border());
    Pixel pix2 = new Pixel(new Color(123, 231, 23), new Border(), new Border(),
            pix1, new Border());
    Pixel pix3 = new Pixel(new Color(12, 155, 254), new Border(), new Border(),
            pix2, new Border());
    Pixel pix4 = new Pixel(new Color(5, 197, 201), new Border(), new Border(),
            pix3, new Border());
    Pixel pix5 = new Pixel(new Color(2, 90, 222), pix1, new Border(), new Border(),
            new Border());
    Pixel pix6 = new Pixel(new Color(2, 90, 222), pix2, new Border(), pix5, new Border());
    Pixel pix7 = new Pixel(new Color(2, 90, 221), pix3, new Border(), pix6, new Border());
    Pixel pix8 = new Pixel(new Color(2, 90, 221), pix4, new Border(), pix7, new Border());
    Pixel pix9 = new Pixel(new Color(139, 32, 215), pix5, new Border(),
            new Border(), new Border());
    Pixel pix10 = new Pixel(new Color(1, 93, 82), pix6, new Border(), pix9, new Border());
    Pixel pix11 = new Pixel(new Color(76, 47, 39), pix7, new Border(), pix10, new Border());
    Pixel pix12 = new Pixel(new Color(127, 118, 121), pix8, new Border(), pix11,
            new Border());
    Pixel pix13 = new Pixel(new Color(125, 132, 113), pix9, new Border(), new Border(),
            new Border());
    Pixel pix14 = new Pixel(new Color(94, 33, 41), pix10, new Border(), pix13, new Border());
    Pixel pix15 = new Pixel(new Color(179, 40, 33), pix11, new Border(), pix14, new Border());
    Pixel pix16 = new Pixel(new Color(180, 76, 67), pix12, new Border(), pix15, new Border());
    Border border = new Border();
    t.checkInexact(pix2.findHEComponent(), 1.396, 0.001);
    t.checkInexact(pix2.findVEComponent(), 2.066, 0.001);
    t.checkInexact(pix2.calculateEnergy(), 1.64, 0.001);
    pix2.up = new Pixel(Color.BLUE);
    pix2.updateEnergies();
    t.checkInexact(pix2.findHEComponent(), 1.729, 0.001);
    t.checkInexact(pix2.findVEComponent(), 2.066, 0.001);
    t.checkInexact(pix2.calculateEnergy(), 0.974, 0.001);
  }

  void testMakeConnectionsPixel(Tester t) {
    APixel pix1 = new Pixel(Color.BLACK);
    APixel up = new Pixel(Color.BLUE);
    APixel down = new Pixel(Color.GRAY);
    APixel left = new Pixel(Color.GREEN);
    APixel right = new Pixel(Color.RED);
    pix1.makeConnectionsPixel(up, down, left, right);
    t.checkExpect(pix1.up, up);
    t.checkExpect(pix1.down, down);
    t.checkExpect(pix1.left, left);
    t.checkExpect(pix1.right, right);
  }

  void testReconnectPixel(Tester t) {
    APixel pix1 = new Pixel(Color.BLACK);
    pix1.up = new Pixel(Color.BLUE);
    pix1.down = new Pixel(Color.GRAY);
    pix1.left = new Pixel(Color.GREEN);
    pix1.right = new Pixel(Color.RED);
    pix1.reconnectPixel();
    t.checkExpect(pix1.up.down, pix1);
    t.checkExpect(pix1.down.up, pix1);
    t.checkExpect(pix1.left.right, pix1);
    t.checkExpect(pix1.right.left, pix1);

    APixel pix2 = new Border();
    pix2.up = new Pixel(Color.BLUE);
    pix2.down = new Pixel(Color.GRAY);
    pix2.left = new Pixel(Color.GREEN);
    pix2.right = new Pixel(Color.RED);
    pix2.reconnectPixel();
    t.checkExpect(pix2.up.down, pix2);
    t.checkExpect(pix2.down.up, pix2);
    t.checkExpect(pix2.left.right, pix2);
    t.checkExpect(pix2.right.left, pix2);
  }

  void testMakeScene(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    SeamCarver im = new SeamCarver(new FromFileImage("images/balloons.jpg"));
    WorldScene imScene = new WorldScene(800, 343);
    imScene.placeImageXY(im.inColor, 400, 171);
    t.checkExpect(im.makeScene(), imScene);

    WorldScene img2Scene = new WorldScene(4, 4);
    img2Scene.placeImageXY(img.inColor, 2, 2);
    t.checkExpect(img.makeScene(), img2Scene);

    SeamCarver bAndW = new SeamCarver(new FromFileImage("images/5x5.png"));
    WorldScene bAndWScene = new WorldScene(5, 5);
    bAndWScene.placeImageXY(bAndW.blackAndWhite, 2, 2);
    t.checkExpect(bAndW.makeScene(), bAndWScene);

    SeamCarver weight = new SeamCarver(new FromFileImage("images/30x30.png"));
    WorldScene weightScene = new WorldScene(30, 30);
    weightScene.placeImageXY(weight.displayEnergies, 15, 15);
    t.checkExpect(weight.makeScene(), weightScene);
  }

  void testOnKeyEvent(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    img.onKeyEvent(" ");
    t.checkExpect(img.paused, false);
    img.onKeyEvent(" ");
    t.checkExpect(img.paused, true);
    img.onKeyEvent("to show that other keys don't do anything");
    t.checkExpect(img.paused, true);
    img.onKeyEvent("v");
    t.checkExpect(img.vCarve, true);
    img.onKeyEvent("h");
    t.checkExpect(img.hCarve, true);
    img.onKeyEvent("u");
    t.checkExpect(img.undoMode, true);
    img.onKeyEvent("w");
    t.checkExpect(img.showWeights, true);
    img.onKeyEvent("e");
    t.checkExpect(img.energySnapShot, true);
  }

  void testOnTick(Tester t) {
    SeamCarver newImg = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    SeamCarver image1 = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    newImg.makeUpdatedImageColor();
    newImg.paintHorizontalSeamRed();
    newImg.paintRed = false;
    newImg.checkIfWellFormed();
    newImg.makeScene();
    image1.onTick();
    t.checkExpect(newImg, image1);

    image1.onTick();
    newImg.makeUpdatedImageColor();
    newImg.seamRemoveHorizontal();
    newImg.seamCarveHorizontal();
    newImg.seamCarveVertical();
    newImg.checkIfWellFormed();
    newImg.makeScene();
    newImg.paintRed = true;
    t.checkExpect(newImg, image1);
  }

  void testSeamCarveVertical(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    SeamCarver newImg = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    APixel imPix1 = img.topLeft.findImageStart();
    APixel imPix2 = img.topLeft.findImageStart().right;
    APixel imPix3 = img.topLeft.findImageStart().right.right;
    APixel imPix4 = img.topLeft.findImageStart().right.right.right;
    APixel imPix5 = img.topLeft.findImageStart().down;
    APixel imPix6 = img.topLeft.findImageStart().down.right;
    APixel imPix7 = img.topLeft.findImageStart().down.right.right;
    APixel imPix8 = img.topLeft.findImageStart().down.right.right.right;
    APixel imPix9 = img.topLeft.findImageStart().down.down;
    APixel imPix10 = img.topLeft.findImageStart().down.down.right;
    APixel imPix11 = img.topLeft.findImageStart().down.down.right.right;
    APixel imPix12 = img.topLeft.findImageStart().down.down.right.right.right;
    APixel imPix13 = img.topLeft.findImageStart().down.down.down;
    APixel imPix14 = img.topLeft.findImageStart().down.down.down.right;
    APixel imPix15 = img.topLeft.findImageStart().down.down.down.right.right;
    APixel imPix16 = img.topLeft.findImageStart().down.down.down.right.right.right;

    newImg.paused = false;
    newImg.seamCarveVertical();

    ArrayList<SeamInfo> ans = new ArrayList<SeamInfo>();
    SeamInfo seam1 = new SeamInfo(imPix1, 1.2827120015924982);
    SeamInfo seam2 = new SeamInfo(imPix2, 1.2861177437813864);
    SeamInfo seam3 = new SeamInfo(imPix3, 1.6392646790898076);
    SeamInfo seam4 = new SeamInfo(imPix4, 1.945801580544551);


    SeamInfo seam5 = new SeamInfo(imPix5, 2.7050363443314227, seam1);
    SeamInfo seam6 = new SeamInfo(imPix6, 2.571211068072641, seam3);
    SeamInfo seam7 = new SeamInfo(imPix7, 2.7556588136119524, seam3);
    SeamInfo seam8 = new SeamInfo(imPix8, 2.9148384759820045, seam3);


    SeamInfo seam9 = new SeamInfo(imPix9, 3.407912450975914, seam6);
    SeamInfo seam10 = new SeamInfo(imPix10, 3.7694728145667122, seam6);
    SeamInfo seam11 = new SeamInfo(imPix11, 3.346452717839014, seam6);
    SeamInfo seam12 = new SeamInfo(imPix12, 3.9189855353337926, seam7);


    SeamInfo seam13 = new SeamInfo(imPix13, 4.816313187737722, seam9);
    SeamInfo seam14 = new SeamInfo(imPix14, 4.667684484478833 , seam11);
    SeamInfo seam15 = new SeamInfo(imPix15, 4.653613501836393, seam11);
    SeamInfo seam16 = new SeamInfo(imPix16, 4.803715218083035 , seam11);
    ans.add(seam13);
    ans.add(seam14);
    ans.add(seam15);
    ans.add(seam16);

    t.checkExpect(img.lastRow.get(0).correspondingInfo, imPix13);
    t.checkExpect(img.lastRow.get(0).totalWeight, 4.816313187737722);
    t.checkExpect(img.lastRow.get(1).correspondingInfo, imPix14);
    t.checkExpect(img.lastRow.get(1).totalWeight, 4.667684484478833);
    t.checkExpect(img.lastRow.get(2).correspondingInfo, imPix15);
    t.checkExpect(img.lastRow.get(2).totalWeight, 4.653613501836393);
    t.checkExpect(img.lastRow.get(3).correspondingInfo, imPix16);
    t.checkExpect(img.lastRow.get(3).totalWeight, 4.803715218083035);
  }

  void testFindMinimumSeam(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    APixel imPix1 = img.topLeft.findImageStart();
    APixel imPix2 = img.topLeft.findImageStart().right;
    APixel imPix3e = img.topLeft.findImageStart().right.right;
    APixel imPix4 = img.topLeft.findImageStart().right.right.right;
    APixel imPix5 = img.topLeft.findImageStart().down;
    APixel imPix6e = img.topLeft.findImageStart().down.right;
    APixel imPix7 = img.topLeft.findImageStart().down.right.right;
    APixel imPix8 = img.topLeft.findImageStart().down.right.right.right;
    APixel imPix9e = img.topLeft.findImageStart().down.down;
    APixel imPix10 = img.topLeft.findImageStart().down.down.right;
    APixel imPix11e = img.topLeft.findImageStart().down.down.right.right;
    APixel imPix12 = img.topLeft.findImageStart().down.down.right.right.right;
    APixel imPix13e = img.topLeft.findImageStart().down.down.down;
    APixel imPix14e = img.topLeft.findImageStart().down.down.down.right;
    APixel imPix15e = img.topLeft.findImageStart().down.down.down.right.right;
    APixel imPix16e = img.topLeft.findImageStart().down.down.down.right.right.right;

    img.paused = false;
    img.seamCarveVertical();

    ArrayList<SeamInfo> ans = new ArrayList<SeamInfo>();
    //SeamInfo seam1 = new SeamInfo(imPix1, 1.2827120015924982);
    //SeamInfo seam2 = new SeamInfo(imPix2, 1.2861177437813864);
    SeamInfo seam3 = new SeamInfo(imPix3e, 1.6392646790898076);
    //SeamInfo seam4 = new SeamInfo(imPix4, 1.945801580544551);


    //SeamInfo seam5 = new SeamInfo(imPix5, 2.7050363443314227, seam1);
    SeamInfo seam6 = new SeamInfo(imPix6e, 2.571211068072641, seam3);
    //SeamInfo seam7 = new SeamInfo(imPix7, 2.7556588136119524, seam3);
    //SeamInfo seam8 = new SeamInfo(imPix8, 2.9148384759820045, seam3);


    SeamInfo seam9 = new SeamInfo(imPix9e, 3.407912450975914, seam6);
    //SeamInfo seam10 = new SeamInfo(imPix10, 3.7694728145667122, seam6);
    SeamInfo seam11 = new SeamInfo(imPix11e, 3.346452717839014, seam6);
    //SeamInfo seam12 = new SeamInfo(imPix12, 3.9189855353337926, seam7);


    SeamInfo seam13 = new SeamInfo(imPix13e, 4.816313187737722, seam9);
    SeamInfo seam14 = new SeamInfo(imPix14e, 4.667684484478833 , seam11);
    SeamInfo seam15 = new SeamInfo(imPix15e, 4.653613501836393, seam11);
    SeamInfo seam16 = new SeamInfo(imPix16e, 4.803715218083035 , seam11);
    ans.add(seam13);
    ans.add(seam14);
    ans.add(seam15);
    ans.add(seam16);

    t.checkExpect(img.findMinimumSeam(ans, 3), seam15);
    t.checkExpect(img.findMinimumSeam(ans, 0), seam14);
    t.checkExpect(img.findMinimumSeam(ans, 2), seam15);
  }

  void testMakeConnections(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    img.makeConnections();
    APixel imPix1 = img.topLeft.findImageStart();
    APixel imPix2 = img.topLeft.findImageStart().right;
    APixel imPix3 = img.topLeft.findImageStart().right.right;
    APixel imPix4 = img.topLeft.findImageStart().right.right.right;
    APixel imPix5 = img.topLeft.findImageStart().down;
    APixel imPix6 = img.topLeft.findImageStart().down.right;
    APixel imPix7 = img.topLeft.findImageStart().down.right.right;
    APixel imPix8 = img.topLeft.findImageStart().down.right.right.right;
    APixel imPix9 = img.topLeft.findImageStart().down.down;
    APixel imPix10 = img.topLeft.findImageStart().down.down.right;
    APixel imPix11 = img.topLeft.findImageStart().down.down.right.right;
    APixel imPix12 = img.topLeft.findImageStart().down.down.right.right.right;
    APixel imPix13 = img.topLeft.findImageStart().down.down.down;
    APixel imPix14 = img.topLeft.findImageStart().down.down.down.right;
    APixel imPix15 = img.topLeft.findImageStart().down.down.down.right.right;
    APixel imPix16 = img.topLeft.findImageStart().down.down.down.right.right.right;
    Border b = new Border();
    t.checkExpect(imPix1.right, imPix2);
    t.checkExpect(imPix10.left, imPix9);
    t.checkExpect(imPix16.up, imPix12);
    t.checkExpect(imPix7.down, imPix11);
  }

  void testPaintVerticalSeamRed(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    img.inColor.setPixel(2, 0, Color.RED);
    img.inColor.setPixel(1, 1, Color.RED);
    img.inColor.setPixel(2, 2, Color.RED);
    img.inColor.setPixel(2, 3, Color.RED);

    SeamCarver img2 = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    img2.paused = false;
    img2.seamCarveVertical();
    img2.paintVerticalSeamRed();

    APixel imPix1 = img2.topLeft.findImageStart();
    APixel imPix2 = img2.topLeft.findImageStart().right;
    APixel imPix3 = img2.topLeft.findImageStart().right.right;
    APixel imPix4 = img2.topLeft.findImageStart().right.right.right;
    APixel imPix5 = img2.topLeft.findImageStart().down;
    APixel imPix6 = img2.topLeft.findImageStart().down.right;
    APixel imPix7 = img2.topLeft.findImageStart().down.right.right;
    APixel imPix8 = img2.topLeft.findImageStart().down.right.right.right;
    APixel imPix9 = img2.topLeft.findImageStart().down.down;
    APixel imPix10 = img2.topLeft.findImageStart().down.down.right;
    APixel imPix11 = img2.topLeft.findImageStart().down.down.right.right;
    APixel imPix12 = img2.topLeft.findImageStart().down.down.right.right.right;
    APixel imPix13 = img2.topLeft.findImageStart().down.down.down;
    APixel imPix14 = img2.topLeft.findImageStart().down.down.down.right;
    APixel imPix15 = img2.topLeft.findImageStart().down.down.down.right.right;
    APixel imPix16 = img2.topLeft.findImageStart().down.down.down.right.right.right;

    t.checkExpect(imPix6.colDisplayed, Color.RED);
    t.checkExpect(imPix11.colDisplayed, Color.RED);
    t.checkExpect(imPix15.colDisplayed, Color.RED);
  }

  void testPaintHorizontalSeamRed(Tester t) {

    SeamCarver img2 = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    img2.paused = false;
    img2.seamCarveHorizontal();
    img2.paintHorizontalSeamRed();

    APixel imPix1 = img2.topLeft.findImageStart();
    APixel imPix2 = img2.topLeft.findImageStart().right;
    APixel imPix3 = img2.topLeft.findImageStart().right.right;
    APixel imPix4 = img2.topLeft.findImageStart().right.right.right;
    APixel imPix5 = img2.topLeft.findImageStart().down;
    APixel imPix6 = img2.topLeft.findImageStart().down.right;
    APixel imPix7 = img2.topLeft.findImageStart().down.right.right;
    APixel imPix8 = img2.topLeft.findImageStart().down.right.right.right;
    APixel imPix9 = img2.topLeft.findImageStart().down.down;
    APixel imPix10 = img2.topLeft.findImageStart().down.down.right;
    APixel imPix11 = img2.topLeft.findImageStart().down.down.right.right;
    APixel imPix12 = img2.topLeft.findImageStart().down.down.right.right.right;
    APixel imPix13 = img2.topLeft.findImageStart().down.down.down;
    APixel imPix14 = img2.topLeft.findImageStart().down.down.down.right;
    APixel imPix15 = img2.topLeft.findImageStart().down.down.down.right.right;
    APixel imPix16 = img2.topLeft.findImageStart().down.down.down.right.right.right;

    t.checkExpect(imPix11.colDisplayed, Color.RED);
    t.checkExpect(imPix12.colDisplayed, Color.RED);
    t.checkExpect(imPix10.colDisplayed, Color.RED);
  }

  void testSeamRemoveVertical(Tester t) {
    SeamCarver img2 = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));

    APixel imPix1 = img2.topLeft.findImageStart();
    APixel imPix2 = img2.topLeft.findImageStart().right;
    APixel imPix3 = img2.topLeft.findImageStart().right.right;
    APixel imPix4 = img2.topLeft.findImageStart().right.right.right;
    APixel imPix5 = img2.topLeft.findImageStart().down;
    APixel imPix6 = img2.topLeft.findImageStart().down.right;
    APixel imPix7 = img2.topLeft.findImageStart().down.right.right;
    APixel imPix8 = img2.topLeft.findImageStart().down.right.right.right;
    APixel imPix9 = img2.topLeft.findImageStart().down.down;
    APixel imPix10 = img2.topLeft.findImageStart().down.down.right;
    APixel imPix11 = img2.topLeft.findImageStart().down.down.right.right;
    APixel imPix12 = img2.topLeft.findImageStart().down.down.right.right.right;
    APixel imPix13 = img2.topLeft.findImageStart().down.down.down;
    APixel imPix14 = img2.topLeft.findImageStart().down.down.down.right;
    APixel imPix15 = img2.topLeft.findImageStart().down.down.down.right.right;
    APixel imPix16 = img2.topLeft.findImageStart().down.down.down.right.right.right;

    img2.paused = false;
    img2.seamRemoveVertical();

    t.checkExpect(imPix5.right, imPix7);
    t.checkExpect(imPix7.left, imPix5);
    t.checkExpect(imPix7.down, imPix10);
    t.checkExpect(imPix10.up, imPix7);
    t.checkExpect(imPix16.left, imPix14);
    t.checkExpect(imPix14.right, imPix16);
    t.checkExpect(imPix7.up, imPix2);
    t.checkExpect(imPix2.down, imPix7);
  }

  void testSeamCarveHorizontal(Tester t) {
    SeamCarver img = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    SeamCarver newImg = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    APixel imPix1 = img.topLeft.findImageStart();
    APixel imPix2 = img.topLeft.findImageStart().right;
    APixel imPix3 = img.topLeft.findImageStart().right.right;
    APixel imPix4 = img.topLeft.findImageStart().right.right.right;
    APixel imPix5 = img.topLeft.findImageStart().down;
    APixel imPix6 = img.topLeft.findImageStart().down.right;
    APixel imPix7 = img.topLeft.findImageStart().down.right.right;
    APixel imPix8 = img.topLeft.findImageStart().down.right.right.right;
    APixel imPix9 = img.topLeft.findImageStart().down.down;
    APixel imPix10 = img.topLeft.findImageStart().down.down.right;
    APixel imPix11 = img.topLeft.findImageStart().down.down.right.right;
    APixel imPix12 = img.topLeft.findImageStart().down.down.right.right.right;
    APixel imPix13 = img.topLeft.findImageStart().down.down.down;
    APixel imPix14 = img.topLeft.findImageStart().down.down.down.right;
    APixel imPix15 = img.topLeft.findImageStart().down.down.down.right.right;
    APixel imPix16 = img.topLeft.findImageStart().down.down.down.right.right.right;

    newImg.paused = false;
    newImg.seamCarveHorizontal();


    t.checkExpect(img.lastCol.get(0).correspondingInfo, imPix4);
    t.checkExpect(img.lastCol.get(0).totalWeight, 4.90066967849218);
    t.checkExpect(img.lastCol.get(1).correspondingInfo, imPix8);
    t.checkExpect(img.lastCol.get(1).totalWeight, 4.342288508174615);
    t.checkExpect(img.lastCol.get(2).correspondingInfo, imPix12);
    t.checkExpect(img.lastCol.get(2).totalWeight, 3.8663713986341834);
    t.checkExpect(img.lastCol.get(3).correspondingInfo, imPix16);
    t.checkExpect(img.lastCol.get(3).totalWeight, 4.160307177156365);
  }

  void testSeamRemoveHorizontal(Tester t) {
    SeamCarver img2 = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));

    APixel imPix1 = img2.topLeft.findImageStart();
    APixel imPix2 = img2.topLeft.findImageStart().right;
    APixel imPix3 = img2.topLeft.findImageStart().right.right;
    APixel imPix4 = img2.topLeft.findImageStart().right.right.right;
    APixel imPix5 = img2.topLeft.findImageStart().down;
    APixel imPix6 = img2.topLeft.findImageStart().down.right;
    APixel imPix7 = img2.topLeft.findImageStart().down.right.right;
    APixel imPix8 = img2.topLeft.findImageStart().down.right.right.right;
    APixel imPix9 = img2.topLeft.findImageStart().down.down;
    APixel imPix10 = img2.topLeft.findImageStart().down.down.right;
    APixel imPix11 = img2.topLeft.findImageStart().down.down.right.right;
    APixel imPix12 = img2.topLeft.findImageStart().down.down.right.right.right;
    APixel imPix13 = img2.topLeft.findImageStart().down.down.down;
    APixel imPix14 = img2.topLeft.findImageStart().down.down.down.right;
    APixel imPix15 = img2.topLeft.findImageStart().down.down.down.right.right;
    APixel imPix16 = img2.topLeft.findImageStart().down.down.down.right.right.right;

    img2.paused = false;
    img2.seamRemoveHorizontal();

    t.checkExpect(imPix6.down, imPix14);
    t.checkExpect(imPix14.up, imPix6);
    t.checkExpect(imPix16.up, imPix8);
    t.checkExpect(imPix8.down, imPix16);
    t.checkExpect(imPix16.left, imPix15);
    t.checkExpect(imPix15.right, imPix16);
    t.checkExpect(imPix7.right, imPix8);
    t.checkExpect(imPix8.left, imPix7);
  }

  void testUndoMove(Tester t) {
    SeamCarver img2 = new SeamCarver(new FromFileImage("images/4x4_Pixel.png"));
    SeamCarver img = img2;

    img.paused = false;

    img2.paused = false;
    img2.seamRemoveHorizontal();
    img2.undoMove();
    t.checkExpect(img, img2);

    img2.seamCarveHorizontal();
    img2.seamCarveVertical();

    img.seamRemoveVertical();
    img.seamCarveVertical();
    img.seamCarveHorizontal();
    img.seamRemoveHorizontal();
    img.undoMove();
    img.undoMove();

    t.checkExpect(img, img2);

    img.undoMove(); // Shouldn't change anything since no removal has been done yet.

    t.checkExpect(img, img2);
  }
}


