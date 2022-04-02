//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.telegram.telegrambots.ApiContextInitializer;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//class Main extends TelegramLongPollingBot {
//    ArrayList<Document> documents = new ArrayList<>();
//    Map<Long, User> userMap = new HashMap<>();
//
//    public static void main(String[] args) {
//        ApiContextInitializer.init();
//        TelegramBotsApi api = new TelegramBotsApi();
//        try {
//            api.registerBot(new Main());
//        } catch (TelegramApiRequestException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void onUpdateReceived(Update update) {
//        Long chatId = null;
//        User user = null;
//        String fromUserMessage = null;
//        if (update.hasMessage()) {
//
//            chatId = update.getMessage().getChatId();
//            fromUserMessage = update.getMessage().getText();
//
//            if (fromUserMessage.equals("/start")) {
//                user = new User(update.getMessage().getFrom().getFirstName());
//                userMap.put(chatId, user);
//            } else {
//                user = userMap.get(chatId);
//            }
//            if (fromUserMessage.equals("Bosh menyuga qaytish")) {
//                user.setRound(0);
//            }
//            if (fromUserMessage.equals("Orqaga")) {
//                if (user.getRound() == 3) {
//                    user.setRound(0);
//                } else {
//                    user.setRound(user.getRound() - 1);
//                }
//            }
//        } else if (update.hasCallbackQuery()) {
//            chatId = update.getCallbackQuery().getMessage().getChatId();
//            user = userMap.get(chatId);
//            fromUserMessage = update.getCallbackQuery().getData();
//            System.out.println(fromUserMessage);
//        }
//
//        if (chatId != null && user != null && fromUserMessage != null) {
//            SendMessage sendMessage = sendMsg(update, user, fromUserMessage);
//            sendMessage.setChatId(chatId);
//            try {
//                execute(sendMessage);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public  SendMessage sendMsg(Update update, User user, String fromUserMessage) {
//        SendMessage sendMessage = new SendMessage();
//        if (user.getRound() != 1) {
//            setButtons(sendMessage, user.getRound());
//        }
//        switch (user.getRound()) {
//            case 0:
//                String text = "";
//                if (update.getMessage().getText().equals("/start")) {
//                    text = "Topilmalar idorasi botiga xush kelibsiz\n";
//                }
//                text = text + "Quyidagi tugmalardan birini bosing";
//                sendMessage.setText(text);
////                setButtons(sendMessage, user.getRound());
//                user.setRound(1);
//                break;
//            case 1:
//                if (update.getMessage().getText().equals("Hujjat topilgan") ||
//                        update.getMessage().getText().equals("Orqaga")) {
//                    sendMessage.setText("Hujjat raqamini kiriting: ");
//                    setButtons(sendMessage, user.getRound());
//                    user.setRound(2);
//                }
//                if (update.getMessage().getText().equals("Hujjat yo'qotilgan")) {
//                    sendMessage.setText("Hujjat turini tanlang");
//                    user.setRound(3);
//                    setButtons(sendMessage, user.getRound());
//                    user.setRound(4);
//                }
//                break;
//            case 2:
//                String toUserMessage = "";
//                for (Document document : documents) {
//                    if (document.getId().toLowerCase().equals(fromUserMessage.toLowerCase())) {
//                        toUserMessage = "Siz qidirgan hujjat: \n";
//                        toUserMessage = toUserMessage + document.toString();
//                        break;
//                    }
//                }
//                if (toUserMessage.length() == 0) {
//                    toUserMessage = "Siz qidirgan hujjat topilmadi";
//                }
////                setButtons(sendMessage, user.getRound());
//                sendMessage.setText(toUserMessage);
//                break;
//            case 3:
////                setButtons(sendMessage, user.getRound());
//                sendMessage.setText("Hujjat turini tanlang:");
//                user.setRound(4);
//                break;
//            case 4:
//
//                Document document=new Document();
//                document.setDocumentType(DocumentType.valueOf(fromUserMessage));
//                user.setDocument(document);
////                setButtons(sendMessage, user.getRound());
//                sendMessage.setText("Hujjat raqamini kiriting:");
//                user.setRound(5);
//                break;
//            case 5:
//                user.getDocument().setId(fromUserMessage);
////                setButtons(sendMessage, user.getRound());
//                sendMessage.setText("Telefon raqamingizni kiriting:");
//                user.setRound(6);
//                break;
//            case 6:
//                Contact contact=new Contact();
//                contact.setPhoneNumber(fromUserMessage);
//                user.getDocument().setContact(contact);
////                setButtons(sendMessage, user.getRound());
//                sendMessage.setText("Ism familiyangizni kiriting:");
//                user.setRound(7);
//                break;
//            case 7:
//                user.getDocument().getContact().setFullname(fromUserMessage);
//                if (documents.add(user.getDocument())) {
////                setButtons(sendMessage, user.getRound());
//                    sendMessage.setText("Sizning ma'lumotlaringiz bazaga kiritildi");
//                } else {
//                    sendMessage.setText("Xatolik yuz berdi ma'lumot saqlanmadi. Boshqatdan kiriting");
//                }
//                user.setRound(0);
//                break;
//        }
//        return sendMessage;
//    }
//
//    public void setButtons(SendMessage sendMessage, int round) {
//        switch (round) {
//            case 0:
//                setReplyKeyboard(sendMessage, "Hujjat topilgan", "Hujjat yo'qotilgan");
//                break;
//            case 1:
//                setReplyKeyboard(sendMessage);
//                break;
//            case 2:
//                setReplyKeyboard(sendMessage, "Orqaga", "Bosh menyuga qaytish");
//                break;
//            case 3:
//                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//                List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//
//
//                DocumentType[] documentTypes = DocumentType.values();
//                for (int i = 0; i < documentTypes.length - 1; i += 2) {
//                    List<InlineKeyboardButton> rowList = new ArrayList<>();
//
//                    InlineKeyboardButton button1 = new InlineKeyboardButton();
//                    button1.setCallbackData(documentTypes[i].name());
//                    button1.setText(documentTypes[i].getNameRu());
//                    rowList.add(button1);
//
//                    InlineKeyboardButton button2 = new InlineKeyboardButton();
//                    button2.setCallbackData(documentTypes[i + 1].name());
//                    button2.setText(documentTypes[i + 1].getNameRu());
//                    rowList.add(button2);
//
//                    keyboard.add(rowList);
//                }
//                inlineKeyboardMarkup.setKeyboard(keyboard);
//                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//                break;
//            case 4:
//            case 5:
//            case 6:
//                setReplyKeyboard(sendMessage);
//                break;
//            case 7:
//                setReplyKeyboard(sendMessage, "Orqaga", "Bosh menyuga qaytish");
//        }
//
//    }
//
//    public void setReplyKeyboard(SendMessage sendMessage, String button1, String button2) {
//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        replyKeyboardMarkup.setSelective(true);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        if (button1.equals("Orqaga")) {
//            replyKeyboardMarkup.setOneTimeKeyboard(false);
//        } else {
//            replyKeyboardMarkup.setOneTimeKeyboard(true);
//        }
//        List<KeyboardRow> keybord = new ArrayList<>();
//        KeyboardRow row = new KeyboardRow();
//        row.add(button1);
//        row.add(button2);
//        keybord.add(row);
//        replyKeyboardMarkup.setKeyboard(keybord);
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//    }
//
//    public void setReplyKeyboard(SendMessage sendMessage) {
//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        replyKeyboardMarkup.setSelective(true);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(false);
//
//        List<KeyboardRow> keybord = new ArrayList<>();
//        KeyboardRow row = new KeyboardRow();
//        row.add("Orqaga");
//        keybord.add(row);
//        replyKeyboardMarkup.setKeyboard(keybord);
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//    }
//
//    public String getBotToken() {
//        return "1336037162:AAF4HL3SGkno-LWrwkj3bysqc08NbpBvz5E";
//    }
//
//    public String getBotUsername() {
//        return "bot_g8_bot";
//    }
//}
//
//
//
//enum DocumentType {
//    PASSPORT("Passport"),
//    TUGULGANLIK_HAQIDA_GUVOHNOMA("Metrika"),
//    HAYDOVCHILIK_GUVOHNOMASI("Prava"),
//    TALABALIK_GUVOHNOMASI("Studencheskiy"),
//    NIKOH_GUVOHNOMASI("Zags"),
//    PLASTIK_CARD("Kartochka"),
//    DOMOVOY("Domovoy"),
//    HARBIY_GUVOHNOMASI ("Voenniy bilet"),
//    DIPLOM("Diplom"),
//    SHAHODATNOMA("Attestat");
//
//    private String nameRu;
//
//    DocumentType(String nameRu) {
//        this.nameRu = nameRu;
//    }
//
//    public String getNameRu() {
//        return nameRu;
//    }
//
//    public void setNameRu(String nameRu) {
//        this.nameRu = nameRu;
//    }
//}
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//class User {
//    private String firstName;
//    private int round;
//    private Document document; //null
//
//    public User(String firstName) {
//        this.firstName = firstName;
//    }
//}
//
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//class Document {
//    private String id;
//    private DocumentType documentType;
//    private Contact contact;
//    private double prise;
//
//
//
//    @Override
//    public String toString() {
//        return  "id:\t'" + id +
//                "\ntype:\t" + documentType +
//                "\ncontact:\t" + contact +
//                "\nprise:\t" + prise;
//
//    }
//}
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//class Contact {
//    private String phoneNumber;
//    private String fullname;
//}
//
