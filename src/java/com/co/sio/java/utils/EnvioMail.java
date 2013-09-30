/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.utils;

import com.co.sio.java.dao.PersonaDao;
import com.co.sio.java.mbeans.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author jcarvajal
 */
public class EnvioMail {

    public static void enviaEmailRegistro(String Asunto, String Destinatario) throws Exception {
        try {
            ConfiguracionBeans x = new ConfiguracionBeans();

            String srvSmtp = x.getSmtp();
            if (!srvSmtp.trim().equalsIgnoreCase("")) {

                Properties props = new Properties();
                props.put("mail.smtp.host", srvSmtp);
                props.setProperty("mail.smtp.auth", "true");
                props.setProperty("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(props, null);
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(x.getRemitente(), "Sistema Administrativo Procedatos"));
                Address[] destTo = InternetAddress.parse(Destinatario);
                if (destTo != null) {
                    msg.setRecipients(Message.RecipientType.TO, destTo);
                }

                msg.setSubject(Asunto);
                String tpEmail = x.getTipoMail();

                if (tpEmail.trim().equalsIgnoreCase("HTML")) {
                    String plain = Utils.fileToString(EnvioMail.class.getResourceAsStream("/com/co/sio/java/utils/Mensajes/mailRegistro.txt"), "UTF-8");
                    msg.setContent(plain, "text/html");
                } else {
                    msg.setText("Prueba");
                }
                Transport transport = session.getTransport("smtp");
                String user = x.getUsuarioMail();
                String passwd = x.getClaveMail();
                transport.connect(srvSmtp, user, passwd);

                if (transport.isConnected()) {
                    transport.sendMessage(msg, destTo);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static void enviaEmailContrasena(String Asunto, String Destinatario, PersonaBeans persona) throws Exception {
        try {
            ConfiguracionBeans x = new ConfiguracionBeans();

            String srvSmtp = x.getSmtp();
            if (!srvSmtp.trim().equalsIgnoreCase("")) {

                Properties props = new Properties();
                props.put("mail.smtp.host", srvSmtp);
                props.setProperty("mail.smtp.auth", "true");
                props.setProperty("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(props, null);
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(x.getRemitente(), "Sistema Administrativo Procedatos"));
                Address[] destTo = InternetAddress.parse(Destinatario);
                if (destTo != null) {
                    msg.setRecipients(Message.RecipientType.TO, destTo);
                }

                msg.setSubject(Asunto);
                String tpEmail = x.getTipoMail();

                if (tpEmail.trim().equalsIgnoreCase("HTML")) {
                    String plain = Utils.fileToString(EnvioMail.class.getResourceAsStream("/com/co/sio/java/utils/Mensajes/mailContrasena_1.txt"), "UTF-8");
                    plain += "Usuario: </strong>" + persona.getIdentificacion() + "</br> " + "";
                    plain += "Contraseña: </strong>" + SeguridadUtils.desencripta(new PersonaDao().recuperarClave(persona)) + "</br> " + "";
                    plain += Utils.fileToString(EnvioMail.class.getResourceAsStream("/com/co/sio/java/utils/Mensajes/mailContrasena_2.txt"), "UTF-8");
                    msg.setContent(plain, "text/html");
                } else {
                    msg.setText("Prueba");
                }
                Transport transport = session.getTransport("smtp");
                String user = x.getUsuarioMail();
                String passwd = x.getClaveMail();
                transport.connect(srvSmtp, user, passwd);

                if (transport.isConnected()) {
                    transport.sendMessage(msg, destTo);
                }
            }


//            ConfiguracionBeans Configuracion = new ConfiguracionBeans();
//            String srvSmtp = Configuracion.getSmtp();
//            if (!srvSmtp.trim().equalsIgnoreCase("")) {
//
//                Properties props = new Properties();
//                props.put("mail.smtp.host", "smtp.gmail.com");
//                props.setProperty("mail.smtp.auth", "true");
//                props.setProperty("mail.smtp.starttls.enable", "true");
//                props.put("mail.smtp.port", "587");
//
//                //Session session = Session.getInstance(props, null);
//                Session session = Session.getInstance(props,
//                        new javax.mail.Authenticator() {
//                            protected PasswordAuthentication getPasswordAuthentication() {
//                                return new PasswordAuthentication("jacagudelo@gmail.com", "deporcali");
//                            }
//                        });
//
//                Message msg = new MimeMessage(session);
//                msg.setFrom(new InternetAddress(ConfiguracionBeans.getRemitente(), "Citas WEB CURU"));
//                Address[] destTo = InternetAddress.parse("jcarvajal@sio.com.co");
//                if (destTo != null) {
//                    msg.setRecipients(Message.RecipientType.TO, destTo);
//                }
//                msg.setSubject(Asunto);
//                String tpEmail = ConfiguracionBeans.getTipoMail();
//
//                if (tpEmail.trim().equalsIgnoreCase("HTML")) {
//                    String plain = Utils.fileToString(EnvioMail.class.getResourceAsStream("./Mensajes/mailContrasena_1.txt"), "UTF-8");
//                    plain = plain + "Contraseña: </strong>" + Contrasena + "</br> " + "";
//                    plain = plain + Utils.fileToString(EnvioMail.class.getResourceAsStream("./Mensajes/mailContrasena_2.txt"), "UTF-8");
//                    msg.setContent(plain, "text/html");
//                } else {
//                    msg.setText("Prueba");
//                }
//                Transport transport = session.getTransport("smtp");
//                String user = ConfiguracionBeans.getUsuarioMail();
//                String passwd = ConfiguracionBeans.getClaveMail();
//                transport.connect("smtp.gmail.com", "jacagudelo@gmail.com", "deporcali");
//
//                if (transport.isConnected()) {
//                    transport.sendMessage(msg, destTo);
//                }
//            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
