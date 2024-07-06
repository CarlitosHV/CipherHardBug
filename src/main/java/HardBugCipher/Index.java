package HardBugCipher;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Scanner;

public class Index {
    static String Cadena = "";
    static byte[] CadenaCifrada;
    static byte[] encryptedText;
    static byte[] encryptedSessionKey;

    public static void main(String[] args) {
        try {
            KeyPair rsaKeyPair = RSAUtils.generateRSAKeyPair();
            PublicKey publicKey = rsaKeyPair.getPublic();
            PrivateKey privateKey = rsaKeyPair.getPrivate();
            SecretKey sessionKey = AESUtils.GenerateSessionKey();
            if (Cadena.length() > 0 && CadenaCifrada != null) {
                System.out.println("Cadena guardada: " + Cadena + ", " + Arrays.toString(CadenaCifrada));
            }

            while (true) {
                LimpiarConsola();
                MostrarMenu(sessionKey, publicKey, privateKey);
            }

        } catch (Exception e) {
            System.out.println("Ha ocurrido un error");
        }
    }

    public static void MostrarMenu(SecretKey sessionKey, PublicKey publicKey, PrivateKey privateKey) {
        LimpiarConsola();
        System.out.println("|-----------OPCIONES------------|");
        System.out.println("|1.- Cifrar texto               |");
        System.out.println("|2.- Decifrar texto             |");
        System.out.println("|3.- Terminar                   |");
        System.out.println("|-------------------------------|");
        System.out.println("Elige una opción:");
        Scanner input = new Scanner(System.in);
        try {
            int optionSelected = input.nextInt();
            switch (optionSelected) {
                case 1: {
                    LimpiarConsola();
                    System.out.println("Ingresa una cadena de texto:");
                    Scanner inputCipher = new Scanner(System.in);
                    String text = inputCipher.nextLine();

                    if (text.length() > 0) {
                        encryptedText = AESUtils.Encrypt(text, sessionKey);
                        encryptedSessionKey = RSAUtils.Encrypt(sessionKey.getEncoded(), publicKey);
                        System.out.println("Texto original: " + text);
                        System.out.println("Texto cifrado: " + encryptedText);
                        CadenaCifrada = encryptedText;
                        Cadena = text;
                    } else {
                        System.out.println("Ingresa una cadena válida");
                    }
                    break;
                }
                case 2: {
                    LimpiarConsola();
                    if (Cadena.length() > 0) {
                        byte[] decryptedSessionKey = RSAUtils.Decrypt(encryptedSessionKey, privateKey);
                        String decryptedMessage = AESUtils.Decrypt(CadenaCifrada, new SecretKeySpec(decryptedSessionKey, "AES"));
                        System.out.println("Cadena decifrada es: " + decryptedMessage);
                    } else {
                        System.out.println("No hay una cadena cargada para decifrar");
                    }
                    break;
                }
                case 3: {
                    System.exit(0);
                }
                default: {
                    LimpiarConsola();
                    System.out.println("No se ha ingresado una opción válida");
                    break;
                }
            }

        } catch (Exception e) {
            LimpiarConsola();
            System.out.println("Opción inválida");
        }
    }

    public static void LimpiarConsola() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                //Runtime.getRuntime().exec("cls");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
