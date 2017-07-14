SET DEFINE OFF;
--CREATE TABLES--

CREATE TABLE WaterResources (
WaterResourcesIndex VARCHAR(4) PRIMARY KEY,
address VARCHAR(35),
geom MDSYS.SDO_GEOMETRY
);

CREATE TABLE Buildings (
building_ID VARCHAR(4) PRIMARY KEY,
building_name VARCHAR(35),
onFire CHAR(1),
no_Residents INT,
floors INT
geom MDSYS.SDO_GEOMETRY
);

CREATE TABLE FireStations (
building_ID VARCHAR(4) PRIMARY KEY,
building_name VARCHAR(35),
geom MDSYS.SDO_GEOMETRY
);

CREATE TABLE Fire_history (
building_ID VARCHAR(4),
Station_ID VARCHAR(35),
fire_date DATE ,
PRIMARY KEY (building_ID, Station_ID, fire_date),
FOREIGN KEY (building_ID) REFERENCES Buildings(building_ID)
ON DELETE CASCADE,
FOREIGN KEY (Station_ID) REFERENCES firestations(building_ID)
ON DELETE CASCADE
);

--UPDATE METADA
INSERT INTO user_sdo_geom_metadata VALUES(
'FireStations',
'geom',
SDO_DIM_ARRAY(SDO_DIM_ELEMENT('Longitude', -180, 180, 0.005), SDO_DIM_ELEMENT('Latitude', -90, 90, 0.005)),
4326
);

INSERT INTO user_sdo_geom_metadata VALUES(
'WaterResources',
'geom',
SDO_DIM_ARRAY(SDO_DIM_ELEMENT('Longitude', -180, 180, 0.005), SDO_DIM_ELEMENT('Latitude', -90, 90, 0.005)),
4326
);

INSERT INTO user_sdo_geom_metadata VALUES(
'Buildings',
'geom',
SDO_DIM_ARRAY(SDO_DIM_ELEMENT('Longitude', -180, 180, 0.005), SDO_DIM_ELEMENT('Latitude', -90, 90, 0.005)),
4326
);
INSERT INTO user_sdo_geom_metadata VALUES(
'Fire_LINK$',
'GEOMETRY',
SDO_DIM_ARRAY(SDO_DIM_ELEMENT('Longitude', -180, 180, 0.005), SDO_DIM_ELEMENT('Latitude', -90, 90, 0.005)),
4326
);

INSERT INTO user_sdo_geom_metadata VALUES(
'Fire_NODE$',
'GEOMETRY',
SDO_DIM_ARRAY(SDO_DIM_ELEMENT('Longitude', -180, 180, 0.005), SDO_DIM_ELEMENT('Latitude', -90, 90, 0.005)),
4326
);

INSERT INTO user_sdo_geom_metadata VALUES(
'Fire_PATH$',
'GEOMETRY',
SDO_DIM_ARRAY(SDO_DIM_ELEMENT('Longitude', -180, 180, 0.005), SDO_DIM_ELEMENT('Latitude', -90, 90, 0.005)),
4326
);


--Create Spatial Indices--
CREATE INDEX WaterResources_index ON WaterResources(geom)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX building_index ON Buildings(geom)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX FS_index ON FireStations(geom)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX NODE_index ON Fire_NODE$(GEOMETRY)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX LINK_index ON Fire_LINK$(GEOMETRY)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX path_index ON Fire_Path$(GEOMETRY)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;