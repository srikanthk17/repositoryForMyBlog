CREATE TABLE IF NOT EXISTS users (
  user_id INTEGER auto_increment PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  name VARCHAR(100) NOT NULL,
  password VARCHAR(50) NOT NULL,
  role_id INTEGER,
  email  VARCHAR(50),
  create_user VARCHAR(100),
  create_date TIMESTAMP,
  update_user VARCHAR(100),
  update_date TIMESTAMP  
);

CREATE TABLE IF NOT EXISTS role (
  role_id INTEGER auto_increment PRIMARY KEY,
  rolename VARCHAR(50),
  role_desc VARCHAR(100),
  create_user VARCHAR(100),
  create_date TIMESTAMP,
  update_user VARCHAR(100),
  update_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS url (
  url_id INTEGER auto_increment PRIMARY KEY,
  name VARCHAR(100),
  description VARCHAR(1000),
  url VARCHAR(1000),
  username VARCHAR(50),
  password VARCHAR(50),
  module_id INTEGER,
  env_id INTEGER,
  role_id INTEGER,		 -- This field indicates the username/password would be visible to only this role. If blank then it would be visible to all if visible is marked as TRUE
  visible BOOLEAN,   	 -- This field indicates if the details is visible to all
  email  VARCHAR(50),
  create_user VARCHAR(100),
  create_date TIMESTAMP,
  update_user VARCHAR(100),
  update_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_url_dtl ( -- This table holds additional URL description
  id INTEGER auto_increment PRIMARY KEY,
  user_id INTEGER,
  url_id INTEGER,
  username VARCHAR(200),
  password VARCHAR(200),
  description VARCHAR(200),
  create_user VARCHAR(100),
  create_date TIMESTAMP,
  update_user VARCHAR(100),
  update_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS module (
  module_id INTEGER auto_increment PRIMARY KEY,
  module_name VARCHAR(100),
  description VARCHAR(200),
  --env_id INTEGER,
  create_user VARCHAR(100),
  create_date TIMESTAMP,
  update_user VARCHAR(100),
  update_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS environment_master (
  env_id INTEGER auto_increment PRIMARY KEY,
  env_name VARCHAR(200),
  env_desc VARCHAR(200),
  create_user VARCHAR(100),
  create_date TIMESTAMP,
  update_user VARCHAR(100),
  update_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS module_env (
  module_env_id INTEGER auto_increment,
  module_id INTEGER,
  env_id INTEGER,
  create_user VARCHAR(100),
  create_date TIMESTAMP,
  update_user VARCHAR(100),
  update_date TIMESTAMP
);

ALTER TABLE users ADD FOREIGN KEY ( role_id ) REFERENCES role( role_id );
ALTER TABLE users ADD CONSTRAINT users_unique UNIQUE(username);
ALTER TABLE url ADD FOREIGN KEY ( role_id ) REFERENCES role( role_id );
ALTER TABLE role ADD CONSTRAINT role_unique UNIQUE(rolename);
ALTER TABLE url ADD FOREIGN KEY ( module_id1 ) REFERENCES module ( module_id );
ALTER TABLE url ADD FOREIGN KEY ( env_id1 ) REFERENCES module ( env_id );
ALTER TABLE user_url_dtl ADD FOREIGN KEY ( user_id ) REFERENCES users ( user_id );
ALTER TABLE user_url_dtl ADD FOREIGN KEY ( url_id ) REFERENCES url ( url_id );
ALTER TABLE module ADD FOREIGN KEY ( env_id ) REFERENCES environment_master( env_id );
ALTER TABLE module_env ADD FOREIGN KEY ( module_id ) REFERENCES module ( module_id );
ALTER TABLE module_env ADD FOREIGN KEY ( env_id ) REFERENCES environment_master( env_id );
ALTER TABLE module_env ADD PRIMARY KEY(module_id, env_id));


INSERT INTO role (role_id, rolename,role_desc) VALUES (1, 'ADMIN', 'Administrator Role');
INSERT INTO role (role_id, rolename,role_desc) VALUES (2, 'GENERAL', 'General Loggedin User');
INSERT INTO users (username,name,password,role_id) VALUES ('ashish', 'Ashish Mondal', 'YXNoaXNo',1);