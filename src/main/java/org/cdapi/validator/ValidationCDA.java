package org.cdapi.validator;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import org.xml.sax.SAXException;

/**
 * Classe responsável por validar o conteúdo XML.
 *
 * @author Gyovanne Cavalcanti
 * @version 1.0
 */
public class ValidationCDA {

    private URL xsdUrl;
    private String notification;
    private String notificationHtml;

    /**
     * Retorna uma representação String do objeto. Em geral, o método
     * {@code getNotification} retorna uma String representando o resultado do
     * processamento de validação do arquivo, se foi bem sucedida ou não.
     * <p>
     * O método {@code getNotification} para a classe {@code ValidationCDA}
     * retorna uma String, que indica o resultado da validação de um documento
     * CDA, conforme o padrão HL7 CDA. O método deve ser instanciado como
     * mostrado na implementação: <blockquote>
     *
     * <pre>
     * {@code getClass().getNotification();}
     * </pre>
     *
     * </blockquote>
     *
     * @return uma String para fins de representação.
     */
    public String getNotification() {
        return notification;
    }

    /**
     * Retorna uma representação String do objeto. Em geral, o método
     * {@code getNotification} retorna uma String representando o resultado do
     * processamento de validação do arquivo, se foi bem sucedida ou não.
     * <p>
     * O método {@code getNotification} para a classe {@code ValidationCDA}
     * retorna uma String, que indica o resultado da validação de um documento
     * CDA, conforme o padrão HL7 CDA. O método deve ser instanciado como
     * mostrado na implementação: <blockquote>
     *
     * <pre>
     * {@code getClass().getNotification();}
     * </pre>
     *
     * </blockquote>
     *
     * @return uma String para fins de representação.
     */
    public String getNotificationHtml() {
        return notificationHtml;
    }

    private void setXsdUrl(URL xsdUrl) {
        this.xsdUrl = xsdUrl;
    }

    private URL localFile() throws IOException {
        return ValidationCDA.class.getResource("/Resources/CDA.xsd");
    }

    /**
     *
     * @return
     */
    public URL getXsdUrl() {
        return xsdUrl;
    }

    private boolean validate(File xml) throws IOException {
        setXsdUrl(localFile());
        if (getXsdUrl() == null) {
            setNotification(xml, false, "Arquivo XSD não encontrado!",
                    "Não foi possivel encontrar o arquivo XSD de origem."
                    + "\nVerifique se a pasta Resources está no diretorio src da aplicação.");
            return false;
        }

        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            URL schemaLocation = getXsdUrl();
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();

            Source source = new StreamSource(xml);

            validator.validate(source);
            setNotification(xml, true, null, null);
            setNotificationHtml(xml, true, null, null);
            return true;

        } catch (SAXException | IOException ex) {
            setNotification(xml, false, ex.getLocalizedMessage(), ex.toString());
            setNotificationHtml(xml, false, ex.getLocalizedMessage(), ex.toString());
            return false;
        }
    }

    private static String getValidationDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy HH:mm:ss");
        Calendar today = Calendar.getInstance();
        return (format.format(today.getTime()));
    }

    /**
     * Retorna uma representação boolean do objeto. Em geral, o método
     * {@code toValidate} retorna um boolean, sendo true para uma validação bem
     * sucedida e false caso não tenha validado com sucesso.
     * <p>
     * O método {@code toValidate} para a classe {@code ValidationCDA} retorna
     * um valor booleano, que indica o resultado da validação de um documento
     * CDA. Após o método receber o arquivo {@code File} como parâmetro, ele
     * verifica se todos os campos do esquema XML corresponde ao padrão HL7 CDA,
     * de acordo com um XSD válido dentro da aplicação. O método deve ser
     * instanciado como mostrado na implementação: <blockquote>
     *
     * <pre>
     * {@code getClass().toValidate(File xml);}
     * </pre>
     *
     * </blockquote>
     *
     * @param xml Arquivo xml ao qual sera validado.
     * @return um valor booleano para fins de verificação.
     * @throws java.io.IOException
     */
    public boolean toValidate(File xml) throws IOException {
        return validate(xml);
    }

    private void setNotification(File xml, boolean value, String message, String detail) {
        this.notification = "Relatório de validação para " + xml.getName() + "\nValidando: esquema XML CDA"
                + "\nData de validação: " + getValidationDate() + "\nResultado do teste: " + value;

        if (value) {
            this.notification += "\n\nValidado\nNenhum problema encontrado!";
        } else {
            this.notification += "\n\nErro de Validação!\n\nMensagem:\n" + message + "\n" + detail;
        }
    }

    private void setNotificationHtml(File xml, boolean value, String message, String detail) {
        this.notificationHtml = "Relatório de validação para " + xml.getName() + "<br>Validando: esquema XML CDA"
                + "<br>Data de validação: " + getValidationDate() + "<br>Resultado do teste: " + value;

        if (value) {
            this.notificationHtml += "<br><br>Validado\nNenhum problema encontrado!";
        } else {
            this.notificationHtml += "<br><br>Erro de Validação!<br><br>Mensagem:<br>" + message + "<br>" + detail;
        }
    }
}
