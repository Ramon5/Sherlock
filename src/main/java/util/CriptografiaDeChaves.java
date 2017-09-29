/*
 * Copyright (c) 2016, Ramon dos Santos Rodrigues
 * Todos os direitos reservados.
 *
 * É permitida a redistribuição e o uso em formulários originais e binários, com ou sem modificação, desde que sejam cumpridas as
 * seguintes condições:
 *
 * 1. Redistribuições do código-fonte devem manter o aviso de direitos autorais acima, esta lista de condições e a seguinte isenção.
 *
 * 2. As redistribuições em formato binário devem reproduzir o aviso de copyright acima, esta lista de condições ea seguinte isenção de 
 * responsabilidade na documentação e / ou outros materiais fornecidos com a distribuição.
 *
 * 3. Nem o nome do detentor dos direitos autorais nem os nomes dos seus contribuidores podem ser utilizados para endossar ou promover 
 * produtos derivados deste software sem autorização prévia específica por escrito.
 *
 * ESTE SOFTWARE É FORNECIDO PELOS DETENTORES DE COPYRIGHT E COLABORADORES "NO ESTADO EM QUE SE ENCONTRAM" E QUAISQUER GARANTIAS 
 * EXPRESSAS OU IMPLÍCITAS, INCLUINDO, MAS NÃO SE LIMITANDO A, GARANTIAS IMPLÍCITAS DE COMERCIABILIDADE E ADEQUAÇÃO A UM PROPÓSITO 
 * ESPECÍFICO. EM NENHUMA CIRCUNSTÂNCIA O PROPRIETÁRIO OU OS CONTRIBUIDORES SERÃO RESPONSÁVEIS POR QUAISQUER DANOS DIRETOS, INDIRETOS, 
 * INCIDENTAIS, ESPECIAIS, EXEMPLARES OU CONSEQÜENCIAIS (INCLUINDO, MAS NÃO SE LIMITANDO À, AQUISIÇÃO DE BENS OU SERVIÇOS SUBSTITUTOS, 
 * PERDA DE USO, DADOS OU LUCROS; OU INTERRUPÇÃO DE NEGÓCIOS), QUALQUER CAUSA E QUALQUER TEORIA DE RESPONSABILIDADE, SEJA POR CONTRATO, 
 * RESPONSABILIDADE ESTRITA OU DANO (INCLUINDO NEGLIGÊNCIA OU QUALQUER OUTRA) DECORRENTE DE QUALQUER FORMA FORA DO USO DESTE SOFTWARE, 
 * MESMO SE AVISADO DA POSSIBILIDADE DE TAIS DANOS.
 *
 */
package util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

public abstract class CriptografiaDeChaves {

    
    private static final String key = "7S@QdSl8Oj$A1N@p";
    private final String iv = "RACTOUFMUGKGZAIG";
    private final Logger logger;

    public CriptografiaDeChaves() {
        logger = Logger.getLogger(CriptografiaDeChaves.class);
    }
    
    public static String getKey(){
        return key;
    }

    protected byte[] encriptar(String texto) {
        try {

            Cipher cifrador = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            SecretKeySpec chave = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cifrador.init(Cipher.ENCRYPT_MODE, chave, new IvParameterSpec(iv.getBytes("UTF-8")));
            return cifrador.doFinal(texto.getBytes());

        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    public String decriptar(byte[] cripto, String key) {
        try {
            Cipher decriptor = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            SecretKeySpec chave = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            decriptor.init(Cipher.DECRYPT_MODE, chave, new IvParameterSpec(iv.getBytes("UTF-8")));
            return new String(decriptor.doFinal(cripto), "UTF-8");

        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getMessage());
        } catch (BadPaddingException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

}
