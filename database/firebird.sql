CREATE TABLE Coleta (
  idColeta BIGINT  NOT NULL  ,
  termo VARCHAR(200)  NOT NULL  ,
  dataColeta TIMESTAMP  NOT NULL    ,
PRIMARY KEY(idColeta));



CREATE TABLE Tweet (
  idTweet BIGINT  NOT NULL  ,
  Coleta_idColeta BIGINT  NOT NULL  ,
  tweet VARCHAR(200)  NOT NULL  ,
  to_user_id BIGINT    ,
  screenname VARCHAR(100)    ,
  user_id BIGINT    ,
  favorite_count INTEGER    ,
  created_at TIMESTAMP  NOT NULL  ,
  lang VARCHAR(4)      ,
PRIMARY KEY(idTweet, Coleta_idColeta)  ,
  FOREIGN KEY(Coleta_idColeta)
    REFERENCES Coleta(idColeta)
      ON DELETE CASCADE
      ON UPDATE NO ACTION);


CREATE INDEX Tweet_FKIndex1 ON Tweet (Coleta_idColeta);




