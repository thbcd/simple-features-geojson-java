package mil.nga.sf.geojson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import mil.nga.sf.geojson.GeoJsonObject;
import mil.nga.sf.geojson.GeoJsonObjectFactory;
import mil.nga.sf.geojson.MultiLineString;
import mil.nga.sf.geojson.Position;

public class MultiLineStringTest {

	private static String MULTILINESTRING = "{\"type\":\"MultiLineString\",\"coordinates\":[[[100.0,10.0],[101.0,1.0],[101.0,10.0]]]}";
	private static String MULTILINESTRING_WITH_ALT = "{\"type\":\"MultiLineString\",\"coordinates\":[[[100.0,10.0,5.0],[101.0,1.0,10.0],[101.0,10.0,15.0]]]}";
	private static String MULTILINESTRING_WITH_MULTIPLE = "{\"type\":\"MultiLineString\",\"coordinates\":[[[-100.0,-50.0],[100.0,-50.0],[1.0,50.0]],[[-50.0,-25.0],[50.0,-25.0],[-1.0,25.0]]]}";
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerializeASFMLS() throws Exception {
		List<mil.nga.sf.LineString> lineStrings = new ArrayList<mil.nga.sf.LineString>();
		List<mil.nga.sf.Position> positions = new ArrayList<mil.nga.sf.Position>();
		positions.add(new Position(100d, 10d));
		positions.add(new Position(101d, 1d));
		positions.add(new Position(101d, 10d));
		mil.nga.sf.LineString lineString = new mil.nga.sf.LineString(positions);
		lineStrings.add(lineString);
		mil.nga.sf.MultiLineString multiLineString = new mil.nga.sf.MultiLineString(lineStrings);
		String text = mapper.writeValueAsString(GeoJsonObjectFactory.createObject(multiLineString));
		assertEquals(MULTILINESTRING, text);
	}

	@Test
	public void itShouldSerializeASFMLSWithAltitude() throws Exception {
		List<mil.nga.sf.LineString> lineStrings = new ArrayList<mil.nga.sf.LineString>();
		List<mil.nga.sf.Position> positions = new ArrayList<mil.nga.sf.Position>();
		positions.add(new Position(100d, 10d,  5d));
		positions.add(new Position(101d,  1d, 10d));
		positions.add(new Position(101d, 10d, 15d));
		mil.nga.sf.LineString lineString = new mil.nga.sf.LineString(positions);
		lineStrings.add(lineString);
		mil.nga.sf.MultiLineString mls = new mil.nga.sf.MultiLineString(lineStrings);
		String text = mapper.writeValueAsString(GeoJsonObjectFactory.createObject(mls));
		assertEquals(MULTILINESTRING_WITH_ALT, text);
	}

	@Test
	public void itShouldSerializeASFMLSWithMultiple() throws Exception {
		List<mil.nga.sf.LineString> lineStrings = new ArrayList<mil.nga.sf.LineString>();
		List<mil.nga.sf.Position> positions = new ArrayList<mil.nga.sf.Position>();
		positions.add(new Position(-100d, -50d));
		positions.add(new Position( 100d, -50d));
		positions.add(new Position(   1d,  50d));
		mil.nga.sf.LineString lineString = new mil.nga.sf.LineString(positions);
		lineStrings.add(lineString);
		positions = new ArrayList<mil.nga.sf.Position>();
		positions.add(new Position(-50d, -25d));
		positions.add(new Position( 50d, -25d));
		positions.add(new Position( -1d,  25d));
		lineString = new mil.nga.sf.LineString(positions);
		lineStrings.add(lineString);
		mil.nga.sf.MultiLineString mls = new mil.nga.sf.MultiLineString(lineStrings);
		String text = mapper.writeValueAsString(GeoJsonObjectFactory.createObject(mls));
		assertEquals(MULTILINESTRING_WITH_MULTIPLE, text);
	}

	@Test
	public void itShouldDeserializeAMLS() throws Exception {
		GeoJsonObject value = mapper.readValue(MULTILINESTRING, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof MultiLineString);
		MultiLineString gjMLS = (MultiLineString)value;
		mil.nga.sf.Geometry geometry = gjMLS.getGeometry();
		assertTrue(geometry instanceof mil.nga.sf.MultiLineString);
		mil.nga.sf.MultiLineString mls = (mil.nga.sf.MultiLineString)geometry;
		List<mil.nga.sf.LineString> lineStrings = mls.getLineStrings();
		assertTrue(lineStrings.size() == 1);
		mil.nga.sf.LineString lineString = lineStrings.get(0);
		List<mil.nga.sf.Position> positions = lineString.getPositions();
		TestUtils.assertPosition(100d, 10d, null, null, positions.get(0));
		TestUtils.assertPosition(101d,  1d, null, null, positions.get(1));
		TestUtils.assertPosition(101d, 10d, null, null, positions.get(2));
	}

	@Test
	public void itShouldDeserializeALineStringWithAltitude() throws Exception {
		GeoJsonObject value = mapper.readValue(MULTILINESTRING_WITH_ALT, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof MultiLineString);
		MultiLineString gjMLS = (MultiLineString)value;
		mil.nga.sf.Geometry geometry = gjMLS.getGeometry();
		assertTrue(geometry instanceof mil.nga.sf.MultiLineString);
		mil.nga.sf.MultiLineString mls = (mil.nga.sf.MultiLineString)geometry;
		List<mil.nga.sf.LineString> lineStrings = mls.getLineStrings();
		assertTrue(lineStrings.size() == 1);
		mil.nga.sf.LineString lineString = lineStrings.get(0);
		List<mil.nga.sf.Position> positions = lineString.getPositions();
		TestUtils.assertPosition(100d, 10d,  5d, null, positions.get(0));
		TestUtils.assertPosition(101d,  1d, 10d, null, positions.get(1));
		TestUtils.assertPosition(101d, 10d, 15d, null, positions.get(2));
	}

	@Test
	public void itShouldDeserializeAMLSWithRings() throws Exception {
		GeoJsonObject value = mapper.readValue(MULTILINESTRING_WITH_MULTIPLE, GeoJsonObject.class);
		assertTrue(value instanceof MultiLineString);
		MultiLineString gjMLS = (MultiLineString)value;
		mil.nga.sf.Geometry geometry = gjMLS.getGeometry();
		assertTrue(geometry instanceof mil.nga.sf.MultiLineString);
		mil.nga.sf.MultiLineString mls = (mil.nga.sf.MultiLineString)geometry;
		List<mil.nga.sf.LineString> lineStrings = mls.getLineStrings();
		assertTrue(lineStrings.size() == 2);
		mil.nga.sf.LineString lineString = lineStrings.get(0);
		List<mil.nga.sf.Position> positions = lineString.getPositions();
		TestUtils.assertPosition(-100d, -50d, null, null, positions.get(0));
		TestUtils.assertPosition( 100d, -50d, null, null, positions.get(1));
		TestUtils.assertPosition(   1d,  50d, null, null, positions.get(2));
		lineString = lineStrings.get(1);
		positions = lineString.getPositions();
		TestUtils.assertPosition(-50d, -25d, null, null, positions.get(0));
		TestUtils.assertPosition( 50d, -25d, null, null, positions.get(1));
		TestUtils.assertPosition( -1d,  25d, null, null, positions.get(2));
	}
}