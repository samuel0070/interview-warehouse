-- categories
INSERT INTO categories (id, category_name, category_type, parent_id, status, created_at, updated_at)
VALUES 
    ('7c23e4d3-bcd3-4f11-91cb-60929251b23a', 'Life Style', 'Clothing', NULL, 1, NOW(), NOW()),
    ('d03fcb96-d10f-41aa-97e2-d5fb3a7948a9', 'Shirt', 'Casual', '7c23e4d3-bcd3-4f11-91cb-60929251b23a', 1, NOW(), NOW()),
    ('cd7474dd-bf60-4485-8dc3-e9529ecec226','Sport ','Equipment',NULL,1,NOW(), NOW()),
	('b281e43b-1da5-4dbd-87ae-69782ec1a0db','Sport indoor','Racket','cd7474dd-bf60-4485-8dc3-e9529ecec226',1,NOW(), NOW());


-- Items
INSERT INTO items (id,"name",description,category_id,category_name,brand_name,base_price,created_at,updated_at) VALUES
	 ('5d7af93b-a3a4-48a9-ad99-2266722dab8f','AIR','new collection','d03fcb96-d10f-41aa-97e2-d5fb3a7948a9','Shirt','nike',150000.00,NOW(), NOW()),
	 ('7563f801-abf5-4c5c-8848-fb737f603ace','Speed Dry','new collection','d03fcb96-d10f-41aa-97e2-d5fb3a7948a9','Shirt','ASICS',180000.00,NOW(), NOW()),
	 ('eed6d88e-122f-451d-af89-0c37d149965f','Raket Padel','new collection','cd7474dd-bf60-4485-8dc3-e9529ecec226','Sport ','Yonex',1800000.00,NOW(), NOW());

-- variants
INSERT INTO variants (id,item_id,variant_name,"size",color,material,sku,price,stock_quantity,"attributes",created_at,updated_at) VALUES
	 ('49a570c5-6401-4d08-ab74-b67cb9a8f6ad','5d7af93b-a3a4-48a9-ad99-2266722dab8f','Pink Cotton AIR L','L','Pink','Cotton','NIKE-PIN-COT-L-2510296',165000.00,10,'{"tipe": "Slimfit", "motif": "tidak ada", "model_kerah": "0"}',NOW(), NOW()),
	 ('8ff705bf-6cd6-452b-9501-4fddd062925b','5d7af93b-a3a4-48a9-ad99-2266722dab8f','Pink Cotton AIR M','M','Pink','Cotton','NIKE-PIN-COT-M-2510296',165000.00,10,'{"tipe": "Slimfit", "motif": "tidak ada", "model_kerah": "0"}',NOW(), NOW()),
	 ('455f8202-de41-44d9-88c3-881c33ccd210','5d7af93b-a3a4-48a9-ad99-2266722dab8f','Pink Cotton AIR S','S','Pink','Cotton','NIKE-PIN-COT-S-2510296',165000.00,10,'{"tipe": "Slimfit", "motif": "tidak ada", "model_kerah": "0"}',NOW(), NOW()),
	 ('fff98aae-ed20-4adc-bbd1-531a54705018','5d7af93b-a3a4-48a9-ad99-2266722dab8f','Black Cotton AIR S','S','Black','Cotton','NIKE-BLA-COT-S-2510296',165000.00,10,'{"tipe": "Slimfit", "motif": "tidak ada", "model_kerah": "0"}',NOW(), NOW()),
	 ('dea262af-8b65-48b6-b0af-77d9cdd5a243','5d7af93b-a3a4-48a9-ad99-2266722dab8f','Black Cotton AIR L','L','Black','Cotton','NIKE-BLA-COT-L-2510296',165000.00,10,'{"tipe": "Slimfit", "motif": "tidak ada", "model_kerah": "0"}',NOW(), NOW()),
	 ('010f50b1-d873-4309-907d-40bad32f00fd','7563f801-abf5-4c5c-8848-fb737f603ace','Black Poly Speed Dry L','L','Black','Poly','ASICS-BLA-POL-L-2510296',195000.00,10,'{"tipe": "Slimfit", "motif": "Batik", "garansi": "30hari", "model_kerah": "0"}',NOW(), NOW()),
	 ('5fc6464c-1401-474b-882e-388b6c2907f1','7563f801-abf5-4c5c-8848-fb737f603ace','Blue Poly Speed Dry L','L','Blue','Poly','ASICS-BLU-POL-L-2510297',195000.00,12,'{"tipe": "Slimfit", "motif": "Batik", "garansi": "30hari", "model_kerah": "0"}',NOW(), NOW()),
	 ('73faab4f-a7fb-4057-a528-4db26592a27a','eed6d88e-122f-451d-af89-0c37d149965f','Black Poly-Metal Raket Padel L','L','Black','Poly-Metal','YONEX-BLA-POL-L-2510296',185000.00,10,'{"tipe": "Handheld", "motif": "Abstract", "garansi": "30hari"}',NOW(), NOW());

--- Inventory LOG
INSERT INTO inventory_logs (id,variant_id,change_type,quantity_change,previous_quantity,new_quantity,reason,created_at) VALUES
	 ('92332758-4648-4861-9fee-b712ccd719c2','73faab4f-a7fb-4057-a528-4db26592a27a','ADJUSTMENT',20,10,30,'test update stock',NOW()),
	 ('950d500f-348c-4a33-9330-5da800314a18','010f50b1-d873-4309-907d-40bad32f00fd','ADJUSTMENT',2,10,12,'test update stock 2',NOW()),
	 ('5eccc998-b090-42e6-8ec7-03e93fb890fa','49a570c5-6401-4d08-ab74-b67cb9a8f6ad','ADJUSTMENT',6,10,16,'test update stock 3',NOW()),
	 ('f808f154-55c2-49f9-81ab-8cb7ad60cd8b','49a570c5-6401-4d08-ab74-b67cb9a8f6ad','STOCK_OUT',-1,16,15,'Sale of 1 units',NOW()),
	 ('cabf2c34-bf5d-45f0-b1bf-5a1b82f38cfe','dea262af-8b65-48b6-b0af-77d9cdd5a243','STOCK_OUT',-7,10,3,'Sale of 7 units',NOW()),
	 ('a0fb0489-5be9-4c05-bd80-6f161f6d0f58','5fc6464c-1401-474b-882e-388b6c2907f1','STOCK_OUT',-1,10,9,'Sale of 1 units',NOW()),
	 ('1f00a024-d81f-49c8-a1e1-08d7189a4dbc','5fc6464c-1401-474b-882e-388b6c2907f1','ADJUSTMENT',3,9,12,'add new stock',NOW());
