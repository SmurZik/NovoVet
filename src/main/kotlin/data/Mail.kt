package data

import state.IllnessHistoryState
import state.PetInfoState
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

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
                return PasswordAuthentication("murca2403@gmail.com", "ttrfyjdasqwbmtdm")
            }
        })
    }

    suspend fun sendMessage(
        illnessHistoryState: IllnessHistoryState,
        visit: List<String>,
        petInfoState: PetInfoState
    ) {
        val message = MimeMessage(Mail().session)
        message.setFrom(InternetAddress("murca2403@gmail.com"))
        var text1 = ""
        var sum = 0
        illnessHistoryState.completedPair().first.forEach {
            if (it != "Услуга") {
                val price = DataImpl().getRealServicePrice(it, illnessHistoryState.visitId())
                sum += price.toInt()
                text1 += "$it - $price\n"
            }
        }
        text1 += "Препараты - ${illnessHistoryState.price() - sum}"
        message.subject = "${visit[14]}_${petInfoState.nickname()}"
        val textPart = MimeBodyPart()
        textPart.setText(text1)
//        val attachmentPart = MimeBodyPart()
//                            attachmentPart.attachFile(
//                                File("C://new//${visit[14]}_${petInfoState.nickname()}.pdf")
//                            )
        val multipart = MimeMultipart()
        multipart.addBodyPart(textPart)
//        multipart.addBodyPart(attachmentPart)
        message.setContent(multipart)
        message.addRecipient(Message.RecipientType.TO, InternetAddress("appadvert66@gmail.com"))
        message.sentDate = Date()
        val transport = Mail().session.getTransport("smtp")
        transport.connect()
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO))
    }
}