import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final double ULLON = MapServer.ROOT_ULLON;
    private static final double ULLAT = MapServer.ROOT_ULLAT;
    private static final double LRLON = MapServer.ROOT_LRLON;
    private static final double LRLAT = MapServer.ROOT_LRLAT;
    private static final double LONG_WIDTH = Math.abs(LRLON - ULLON);
    private static final double LAT_HEIGHT = Math.abs(ULLAT - LRLAT);
    private static final double TILE_SIZE = MapServer.TILE_SIZE;
    public Rasterer() {
        // YOUR CODE HERE

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public int commuteDepth(double ullon, double lrlon, double width) {
        // Calculate the LonDPP of the query box
        double queryLonDPP = (lrlon - ullon) / width;
        // Calculate the LonDPP of the root image
        double rootLonDPP = LONG_WIDTH / TILE_SIZE;
        // Determine the depth based on the LonDPP
        int depth = 0;
        while (depth < 7 && rootLonDPP > queryLonDPP) {
            depth++;
            rootLonDPP /= 2; // Each increase in depth halves the LonDPP
        }
        return depth;
    }
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        //System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
        //                   + "your browser.");
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double width = params.get("w");
        double height = params.get("h");
        int depth = commuteDepth(ullon, lrlon, width);
        boolean querySuccess = true;
        if (lrlon <= ULLON || ullon >= LRLON || lrlat >= ULLAT || ullat <= LRLAT
            || depth > 7 || ullon >= lrlon || ullat <= lrlat) {
            querySuccess = false;
        }
        double widthPerTile = LONG_WIDTH / Math.pow(2, depth);
        double heightPerTile = LAT_HEIGHT / Math.pow(2, depth);
        int xStart = (int) ((ullon - ULLON) / widthPerTile);
        int xEnd = (int) ((lrlon - ULLON) / widthPerTile);
        int yStart = (int) ((ULLAT - ullat) / heightPerTile);
        int yEnd = (int) ((ULLAT - lrlat) / heightPerTile);

        double xStartLon = ULLON + xStart * widthPerTile;
        double xEndLon = ULLON + (xEnd + 1) * widthPerTile;
        double yStartLat = ULLAT - yStart * heightPerTile;
        double yEndLat = ULLAT - (yEnd + 1) * heightPerTile;

        String[][] renderGrid = new String[yEnd - yStart + 1][xEnd - xStart + 1];
        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                renderGrid[y - yStart][x - xStart] = "d" + depth + "_x" + x + "_y" + y + ".png";
            }
        }
        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", xStartLon);
        results.put("raster_ul_lat", yStartLat);
        results.put("raster_lr_lon", xEndLon);
        results.put("raster_lr_lat", yEndLat);
        results.put("depth", depth);
        results.put("query_success", querySuccess);
        return results;
    }

}
