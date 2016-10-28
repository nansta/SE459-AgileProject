package floor;

import general.DataValidationException;

import java.util.ArrayList;
import java.util.List;

import static floor.Tile.Direction.*;

import map.Point;

public class Tile {

	private Tile north, south, east, west;
	
	private int visited;
	private int dirt;

	private Point coordinates;
	
	Floor floor;
	Role role;

	public enum Direction {
		NORTH,
		SOUTH,
		EAST,
		WEST;
		
		public Direction getOpposite(Direction direction) throws DataValidationException {
			switch (direction) {
			case NORTH:
				return Direction.SOUTH;
			case SOUTH:
				return NORTH;
			case EAST:
				return Direction.WEST;
			case WEST:
				return Direction.EAST;
				
			default:
				throw new DataValidationException("ERROR: Invalid direction");
			}
		}
	}
	
	public enum Floor {
		BARE(1),
		LOW(2),
		HIGH(3);

		private final int floorCode;

		Floor(int floorCodeIn) {
			floorCode = floorCodeIn;
		}

		public int getFloorCode() {
			return floorCode;
		}

		public String getFloorCodeAsString() {
			switch (floorCode) {
			case 1:
				return "Bare floor";
			case 2:
				return "Low pile";
			case 3:
				return "High pile";

			default:
				return null;	// TODO: Throw exception?
			}
		}
	}
	
	public enum Role {
		CHARGE,
		BASE;
	}

	public Tile(int dirtIn, Floor floorIn) {
		dirt = dirtIn;
		floor = floorIn;
		visited = 0;
	}
	
	public Tile(Floor floorIn) {
		dirt = 0;
		floor = floorIn;
		visited = 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dirt: " + dirt);
		sb.append("\nFloor type: " + floor.getFloorCodeAsString());

		return sb.toString();
	}

	public void attachTile(Tile tile, Direction direction) throws DataValidationException {
		switch (direction) {
		case NORTH:
			this.north = tile;
			tile.south = this;
			break;

		case SOUTH:
			this.south = tile;
			tile.north = this;
			break;

		case EAST:
			this.east = tile;
			tile.west = this;
			break;

		case WEST:
			this.west = tile;
			tile.east = this;
			break;

		default:
			throw new DataValidationException("ERROR: Invalid direction");
		}
	}
	
	public void detachTile(Direction direction) throws DataValidationException {
		Tile other;
		
		switch (direction) {
		case NORTH:
			other = getAdjacent(NORTH);
			this.north = null;
			other.south = null;
			break;

		case SOUTH:
			other = getAdjacent(SOUTH);
			this.south = null;
			other.north = null;
			break;

		case EAST:
			other = getAdjacent(EAST);
			this.east = null;
			other.west = null;
			break;

		case WEST:
			other = getAdjacent(WEST);
			this.west = null;
			other.east = null;
			break;

		default:
			throw new DataValidationException("ERROR: Invalid direction");
		}
	}

	public void visit() {
		visited ++;
	}
	
	public boolean isChargingStation() {
		if (role == Role.BASE || role == Role.CHARGE) {
			return true;
		}
		
		return false;
	}
	
	public int getVisited() {
		return visited;
	}

	public boolean hasDirt() {
		return (dirt > 0);
	}

	public Floor getFloor() {
		return floor;
	}
	
	public Tile getAdjacent(Direction direction) throws DataValidationException {
		switch (direction) {
		case NORTH:
			return north;
		case SOUTH:
			return south;
		case EAST:
			return east;
		case WEST:
			return west;

		default:
			throw new DataValidationException("ERROR: Invalid direction");
		}
	}

	/**
	 * Gets list of adjacent tiles that exist.
	 *
	 * @return List of tiles.
	 */
	public List<Tile> getAdjacentTiles() {
		List<Tile> adjacents = new ArrayList<>();

        if (north != null) adjacents.add(north);
        if (south != null) adjacents.add(south);
        if (east != null) adjacents.add(east);
        if (west != null) adjacents.add(west);

		return adjacents;
	}

	/**
	 * Provides char direction to get to an adjacent tile.
	 * @param tile Tile in which the direction to should be retrieved.
	 * @return Char of direction.
	 * @throws DataValidationException
	 */
	public Direction getDirectionTo(Tile tile)  {
		try {
			if (this.getAdjacent(NORTH) == tile) {
                return NORTH;
            } else if (this.getAdjacent(EAST) == tile) {
                return EAST;
            } else if (this.getAdjacent(SOUTH) == tile) {
                return SOUTH;
            } else if (this.getAdjacent(WEST) == tile) {
                return WEST;
            } else {
                throw new DataValidationException("ERROR: Direction unknown");
            }
		} catch (DataValidationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void clean() {
		dirt --;
	}
	
	public void setFloor(Floor floorIn) {
		floor = floorIn;
	}
	
	public void setRole(Role roleIn) {
		role = roleIn;
	}

	public void setCoordinates(Point coordinatesIn) {
		coordinates = coordinatesIn;
	}

	public Point getCoordinates() {
		return coordinates;
	}

}
