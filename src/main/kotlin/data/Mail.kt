package data

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

class Mail {

    var session: Session

    init {
        val props = Properties()
//        props.put("mail.transport.protocol", "smtps")
        props.put("mail.smtp.host", "smtp.gmail.com")
        props.put("mail.smtp.port", "587")
//        props.put("mail.smtp.user", "smur2in")
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
//        props.put("mail.smtp.ssl.enable", "true")
        props.put("mail.debug", "true")

        session = Session.getDefaultInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("novovetsend@gmail.com", "dsshgpdupwwhetjn")
            }
        })
    }
}