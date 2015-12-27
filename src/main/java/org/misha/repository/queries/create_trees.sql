#################################################################
##################### mysql #####################################
#################################################################

DROP DATABASE IF EXISTS TREES;
CREATE DATABASE IF NOT EXISTS TREES;
USE TREES;

DROP TABLE IF EXISTS NODES;
DROP TABLE IF EXISTS NODE_DATA;
DROP TABLE IF EXISTS POLYNOMIALS;

CREATE TABLE IF NOT EXISTS POLYNOMIALS (
  polynomial_id BIGINT,
  monomial_id   BIGINT,
  scalar        INT,
  is_final      BOOLEAN,
  INDEX (`polynomial_id`, `monomial_id`)
);

CREATE TABLE IF NOT EXISTS NODES (
  node_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
  left_id  BIGINT,
  right_id BIGINT,
  data_id  BIGINT
);

CREATE TABLE IF NOT EXISTS NODE_DATA (
  node_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
  data_value VARCHAR(200) UNIQUE,
  INDEX (`data_value`(12))
);

DROP PROCEDURE IF EXISTS new_node_letter;
CREATE PROCEDURE new_node_letter(IN node_data VARCHAR(200))
  BEGIN
    INSERT INTO NODE_DATA VALUES (NULL, node_data);
    INSERT INTO NODES VALUES (NULL, left_id, right_id, LAST_INSERT_ID());
  END;

DROP PROCEDURE IF EXISTS alphabet;
CREATE PROCEDURE alphabet()
  BEGIN
    DECLARE v_max INT UNSIGNED DEFAULT 26;
    DECLARE v_count INT UNSIGNED DEFAULT 0;
    WHILE (v_count < v_max) DO
      CALL new_node_letter(
          ELT(v_count + 1, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
              's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
              'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          ));
      SET v_count = v_count + 1;
    END WHILE;
  END;

CALL alphabet();

DROP PROCEDURE IF EXISTS new_node;
CREATE PROCEDURE new_node(IN left_id INT, IN right_id INT)
  BEGIN
    DECLARE left_data VARCHAR(200) DEFAULT NULL;
    DECLARE right_data VARCHAR(200) DEFAULT NULL;
    DECLARE to_insert VARCHAR(200) DEFAULT NULL;
    SELECT data_value
    INTO left_data
    FROM NODE_DATA
    WHERE node_id = left_id;
    SELECT data_value
    INTO right_data
    FROM NODE_DATA
    WHERE node_id = right_id;
    SET to_insert = CONCAT(CONCAT(CONCAT(CONCAT('[', left_data), ', '), right_data), ']');
    INSERT INTO NODE_DATA VALUES (NULL, to_insert);
    INSERT INTO NODES VALUES (NULL, left_id, right_id, LAST_INSERT_ID());
  END;

########################### TEST ####################################################
SET @left_id = 2;
SET @right_id = 1;
SET @data = NULL;
CALL new_node(@left_id, @right_id);
########################################################################################

DROP PROCEDURE IF EXISTS new_polynomial;
CREATE PROCEDURE new_polynomial()
  BEGIN
    DECLARE polynomial_count BIGINT DEFAULT 0;
    SELECT count(1)
    FROM POLYNOMIALS
    INTO polynomial_count;
    INSERT INTO POLYNOMIALS VALUES (polynomial_count + 1, NULL, 0, FALSE);
  END;
CALL new_polynomial();

DROP PROCEDURE IF EXISTS update_polynomial;
CREATE PROCEDURE update_polynomial(IN in_polynomial_id BIGINT, IN in_monomial_id BIGINT, IN in_scalar INT)
  BEGIN
    DECLARE current_monomial_id BIGINT;
    DECLARE current_scalar INT;
    DECLARE is_done BOOLEAN DEFAULT FALSE;
    DECLARE is_updated BOOLEAN DEFAULT FALSE;
    DECLARE curs CURSOR FOR (
      SELECT
        POLYNOMIALS.monomial_id,
        POLYNOMIALS.scalar
      FROM POLYNOMIALS
      WHERE polynomial_id = in_polynomial_id);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET is_done = TRUE;
    OPEN curs;
    myloop: WHILE NOT is_done DO
      FETCH curs
      INTO current_monomial_id, current_scalar;
      IF current_monomial_id IS NULL
      THEN #first update after creation
        SET is_done = TRUE;
        SET is_updated = TRUE;
        UPDATE POLYNOMIALS
        SET scalar = in_scalar, monomial_id = in_monomial_id
        WHERE polynomial_id = in_polynomial_id;
      ELSEIF current_monomial_id = in_monomial_id
        THEN #collect similar summands
          SET is_done = TRUE;
          SET is_updated = TRUE;
          UPDATE POLYNOMIALS
          SET scalar = (in_scalar + current_scalar)
          WHERE polynomial_id = in_polynomial_id AND monomial_id = in_monomial_id;
      END IF;
    END WHILE;
    IF NOT is_updated
    THEN
      INSERT INTO POLYNOMIALS VALUES (in_polynomial_id, in_monomial_id, in_scalar, FALSE);
    END IF;
    CLOSE curs;
  END;
############################ TEST ########################################
SET @polynomial_id = 1;
SET @_monomial_id = 1;
SET @constant = 1;
CALL update_polynomial(@polynomial_id, @_monomial_id, @constant);
SELECT *
FROM POLYNOMIALS;
########################################################################

DROP PROCEDURE IF EXISTS finalize_polynomial;
CREATE PROCEDURE finalize_polynomial(IN in_polynomial_id BIGINT)
  BEGIN
    UPDATE POLYNOMIALS
    SET is_final = TRUE
    WHERE polynomial_id = in_polynomial_id;
  END;

###################### TEST ###########################################
SELECT
  n.node_id,
  n.left_id,
  n.right_id,
  d.data_value
FROM NODES n JOIN NODE_DATA d ON (n.node_id = d.node_id);
#######################################################################
SELECT *
FROM NODES