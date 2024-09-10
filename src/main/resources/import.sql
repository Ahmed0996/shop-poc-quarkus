

--insert values in item table

INSERT INTO item (name , description ,quantity , price ) VALUES('chair' , 'wood chair' , 5 , 12.5 );
INSERT INTO item (name , description ,quantity  , price ) VALUES('banana' , 'fruit type' , 6 , 4.99 );
INSERT INTO item (name , description ,quantity , price ) VALUES('chair2' , 'wood chai2r' , 7 , 12.5 );
INSERT INTO item (name , description ,quantity , price ) VALUES('chair3' , 'wood chair3' , 8 , 12.5 );
INSERT INTO item (name , description ,quantity , price ) VALUES('chair4' , 'wood chair4' , 11 , 8 );


-- insert values in shop address
INSERT INTO shopaddress (name, shortdescription, latitude, longitude)
VALUES ('Shop 1', 'Store 1', 40.7128, -74.0060);

INSERT INTO shopaddress (name, shortdescription, latitude, longitude)
VALUES ('Shop 2', 'Store 2', 34.0522, -118.2437);
INSERT INTO shopaddress (name, shortdescription, latitude, longitude)
VALUES ('Shop 3', 'Store 3', 26.7128, -7.0060);

INSERT INTO shopaddress (name, shortdescription, latitude, longitude)
VALUES ('Shop 4', 'Store 4', 3.0522, -18.2437);
INSERT INTO shopaddress (name, shortdescription, latitude, longitude)
VALUES ('Shop 5', 'Store 5', 4.7128, -4.0060);


-- insert values in client address
INSERT INTO clientaddress (name, shortdescription, latitude, longitude, isdefault , client_id)
VALUES ('Client 1', 'Home Address', 37.7749, -122.4194, true , 1);

INSERT INTO clientaddress (name, shortdescription, latitude, longitude, isdefault , client_id)
VALUES ('Client 2', 'Work Address', 40.7306, -73.9352, false , 2);

INSERT INTO clientaddress (name, shortdescription, latitude, longitude, isdefault , client_id)
VALUES ('Client 3', 'Vacation Home', 33.4484, -112.0740, false , 3);

INSERT INTO clientaddress (name, shortdescription, latitude, longitude, isdefault , client_id)
VALUES ('Client 4', 'Secondary Home', 47.6062, -122.3321, true , 4);

-- insert values in cart address
INSERT INTO orderaddress (name, shortdescription, latitude, longitude)
VALUES ('Cart 1', 'Warehouse Location', 35.6895, 139.6917);

INSERT INTO orderaddress (name, shortdescription, latitude, longitude)
VALUES ('Cart 2', 'Shipping Location', 55.7558, 37.6173);

INSERT INTO orderaddress (name, shortdescription, latitude, longitude)
VALUES ('Cart 3', 'Distribution Center', -33.8688, 151.2093);

INSERT INTO orderaddress (name, shortdescription, latitude, longitude)
VALUES ('Cart 4', 'Fulfillment Center', -34.6037, -58.3816);

INSERT INTO orderaddress (name, shortdescription, latitude, longitude)
VALUES ('Cart 5', 'Fulfillment Center', -38.6037, -53.3816);

INSERT INTO orderaddress (name, shortdescription, latitude, longitude)
VALUES ('Cart 6', 'Fulfillment Center', -24.6037, -48.3816);

INSERT INTO orderaddress (name, shortdescription, latitude, longitude)
VALUES ('Cart 7', 'Fulfillment Center', -14.6037, -58.3816);

INSERT INTO orderaddress (name, shortdescription, latitude, longitude)
VALUES ('Cart 8', 'Fulfillment Center', -34.6037, 58.3816);


-- insert values in shop
--paid
INSERT INTO shop (name, shop_address_id)
VALUES ('Shop 1', 1);

-- paid
INSERT INTO shop (name, shop_address_id)
VALUES ('Shop 2', 2);




INSERT INTO client (id ,name, email, phone , isactive , isdeleted)
VALUES (1 , 'Client 1', 'client1@example.com' , '1234567890' , true , false);

INSERT INTO client (id ,name, email, phone , isactive , isdeleted)
VALUES (2 , 'Client 2', 'client2@example.com' , '1234567890' , true , false);

INSERT INTO client (id ,name, email, phone , isactive , isdeleted)
VALUES (3 , 'Client 3', 'client3@example.com' , '1234567890' , true , false);

INSERT INTO client (id ,name, email, phone , isactive , isdeleted)
VALUES (4 , 'Client 4', 'client4@example.com' , '1234567890' , true , false);


-- insert into cart shop 1
INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('f6cdcf3f-e52f-49b0-8056-05f0f62009e2'::UUID , 'ON_DELIVERY' , 1 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('2f1449d7-26b2-43e3-83b2-5dbd7b36f48f'::UUID  , 'ON_DELIVERY' , 1 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('f8a06951-5b04-4a60-b121-ebfbf5fa6d51'::UUID , 'DRAFT' , 1 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('cb9ca4b3-c190-410d-9221-96c548d57b31'::UUID , 'ON_DELIVERY' , 1, 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('0dca193a-2cd3-4683-8187-8f201d38a7f7'::UUID , 'DRAFT' , 1 , 100 , 1 );


-- insert into cart shop 2
INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('f6cdcf3f-e52f-49b0-8056-05f0f63009e2'::UUID , 'ON_DELIVERY' , 2 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('2f1449d7-26b2-43e3-83b2-5dbd3b36f48f'::UUID  , 'ON_DELIVERY' , 2 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('f8a06951-5b04-4a60-b121-ebfbf53a6d52'::UUID , 'DRAFT' , 2 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('cb9ca4b3-c190-410d-9221-96c938d37b31'::UUID , 'ON_DELIVERY' , 2 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('0dca193a-2cd3-4683-8187-8f201338a7f7'::UUID , 'DRAFT' , 2 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('cb9ca4b3-c190-410d-9221-96c938d37b34'::UUID , 'ON_DELIVERY' , 2 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('cb9ca4b3-c190-410d-9221-96c938d37b32'::UUID , 'ON_DELIVERY' , 2 , 100 , 1 );

INSERT INTO cart (id , status , shop_id ,totalamount , client_id )
VALUES('cb9ca4b3-c190-410d-9221-96c938d37b33'::UUID , 'ON_DELIVERY' , 2 , 100 , 1 );

-- cart item
INSERT INTO cart_item (cart_id , item_id )
VALUES('f6cdcf3f-e52f-49b0-8056-05f0f62009e2'::UUID , 1 ),
('2f1449d7-26b2-43e3-83b2-5dbd7b36f48f'::UUID , 2 ),
('f8a06951-5b04-4a60-b121-ebfbf5fa6d51'::UUID , 3 ),
('cb9ca4b3-c190-410d-9221-96c938d37b31'::UUID , 4 ),
('0dca193a-2cd3-4683-8187-8f201d38a7f7'::UUID , 5 );



-- insert values in orders for each shop
--INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
--VALUES(gen_random_uuid() , 'PAID' , 1 , '2f1449d7-26b2-43e3-83b2-5dbd7b36f48f' , 1 , 5.5 , now() );

INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'PAID' , 1 , '2f1449d7-26b2-43e3-83b2-5dbd7b36f48f' , 2 , 6.5 , now()  );


INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'DRAFT' , 1 , 'f8a06951-5b04-4a60-b121-ebfbf5fa6d51' , 3 ,10 , now() );

INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'PAID' , 1 , 'cb9ca4b3-c190-410d-9221-96c548d57b31' , 4 , 20 , now()  );

INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'DRAFT' , 1 , '0dca193a-2cd3-4683-8187-8f201d38a7f7' , 4 , 20 , now() );


-- insert values in orders for each shop
INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'PAID' , 2 , 'f6cdcf3f-e52f-49b0-8056-05f0f63009e2' , 1 , 5.5 , now() );

INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'PAID' , 2 , '2f1449d7-26b2-43e3-83b2-5dbd3b36f48f' , 2 , 6.5 , now()  );


INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'DRAFT' , 2 , 'f8a06951-5b04-4a60-b121-ebfbf53a6d52' , 3 ,10 , now() );

INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'PAID' , 2, 'cb9ca4b3-c190-410d-9221-96c938d37b31' , 4 , 20 , now()  );

INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'DRAFT' , 2 , '0dca193a-2cd3-4683-8187-8f201338a7f7' , 5 , 20 , now() );

INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'PAID' , 2, 'cb9ca4b3-c190-410d-9221-96c938d37b34' , 6 , 20 , now()  );

INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
VALUES(gen_random_uuid() , 'PAID' , 2, 'cb9ca4b3-c190-410d-9221-96c938d37b32' , 7 , 20 , now()  );

--INSERT INTO orders(id , status , shop_id , cart_id , order_address_id , distance , createdat)
--VALUES(gen_random_uuid() , 'PAID' , 2, 'cb9ca4b3-c190-410d-9221-96c938d37b33' , 8 , 20 , now()  );


