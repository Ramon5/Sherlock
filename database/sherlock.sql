CREATE TABLE Coleta (
  idColeta BIGINT  NOT NULL  ,
  termo VARCHAR(100)  NOT NULL  ,
  dataColeta TIMESTAMP  NOT NULL    ,
PRIMARY KEY(idColeta));



CREATE TABLE Autorizacao (
  idAuth BIGINT  NOT NULL  ,
  chave VARCHAR(20)  NOT NULL    ,
PRIMARY KEY(idAuth));



CREATE TABLE Tweet (
  idTweet BIGINT  NOT NULL  ,
  Coleta_idColeta BIGINT  NOT NULL  ,
  tweet VARCHAR(200)  NOT NULL  ,
  to_user_id BIGINT    ,
  screenname VARCHAR(150)    ,
  user_id BIGINT    ,
  favorite_count INTEGER    ,
  created_at TIMESTAMP  NOT NULL  ,
  retweet INTEGER    ,
  latitude DOUBLE    ,
  longitude DOUBLE    ,
  lang CHAR(2)      ,
   api VARCHAR(15) ,
PRIMARY KEY(idTweet, Coleta_idColeta)  ,
  FOREIGN KEY(Coleta_idColeta)
    REFERENCES Coleta(idColeta)
      ON DELETE CASCADE
      ON UPDATE NO ACTION);


CREATE INDEX Tweet_FKIndex1 ON Tweet (Coleta_idColeta);



CREATE TABLE Chave (
  idChave BIGINT  NOT NULL  ,
  Autorizacao_idAuth BIGINT  NOT NULL  ,
  consumer_key VARCHAR(255)  NOT NULL  ,
  consumer_secret VARCHAR(255)  NOT NULL  ,
  accessToken VARCHAR(255)  NOT NULL  ,
  accessSecret VARCHAR(255)  NOT NULL    ,
PRIMARY KEY(idChave, Autorizacao_idAuth)  ,
  FOREIGN KEY(Autorizacao_idAuth)
    REFERENCES Autorizacao(idAuth)
      ON DELETE CASCADE
      ON UPDATE NO ACTION);


CREATE INDEX Chave_FKIndex1 ON Chave (Autorizacao_idAuth);




