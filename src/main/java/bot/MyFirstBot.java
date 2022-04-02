package bot;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


class MyFirstBot extends TelegramLongPollingBot {
    Double usd;
    Double eur;
    Double cny;
    Map<Long, Integer> userMap = new HashMap<>();
    Integer current= 0;

    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(new MyFirstBot());
    }

    @SneakyThrows
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        for (Currency currency : currencies()) {
            if (currency.getCcy().equalsIgnoreCase("eur")){
                this.eur = currency.getRate();
            } if (currency.getCcy().equalsIgnoreCase("usd")){
                this.usd = currency.getRate();
            }
            if (currency.getCcy().equalsIgnoreCase("cny")){
                this.cny = currency.getRate();
            }
        }
        Long chatId = update.getMessage().getChatId();
        String fromUser = update.getMessage().getText();
        userMap.putIfAbsent(chatId, 0);


        switch (fromUser) {
            case "/start":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 0);
                        this.current = integer;
                    }
                });
                break;
            case "UZS -> EUR":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 1);
                        this.current = integer;
                    }
                });
                break;
            case "UZS -> USD":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 2);
                        this.current = integer;
                    }
                });
                break;
            case "USD -> UZS":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 3);
                        this.current = integer;
                    }
                });
                break;
            case "EUR -> UZS":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 4);
                        this.current = integer;
                    }
                });
                break;
            case "UZS -> CNY":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 5);
                        this.current = integer;
                    }
                });
                break;
            case "CNY -> UZS":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 6);
                        this.current = integer;
                    }
                });
                break;
        }
        userMap.forEach((aLong, integer) -> {
            if (chatId.equals(aLong)) {
                current = integer;
            }
        });


        execute(mainKeyboard(update));
    }

    public String getBotToken() {
        return "1465022861:AAESsyfgTVyEuLnKiBeGtadJqilVAggKJsA";
    }

    public String getBotUsername() {
        return "@NewCurrencybot_bot";
    }

    public SendMessage mainKeyboard(Update update) throws TelegramApiException {
        switch (current) {
            case 1:
                execute(uzsEur(update));
                break;
            case 2:
                execute(uzsUsd(update));
                break;
            case 3:
                execute(usdUzs(update));
                break;
            case 4:
                execute(eurUzs(update));
                break;
            case 5:
                execute(uzsCny(update));
                break;
            case 6:
                execute(cnyUzs(update));
                break;

        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keybord = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keybord);
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add("UZS -> USD");
        row1.add("USD -> UZS");
        row2.add("UZS -> EUR");
        row2.add("EUR -> UZS");
        row3.add("UZS -> CNY");
        row3.add("CNY -> UZS");
        keybord.add(row1);
        keybord.add(row2);
        keybord.add(row3);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        String fromUser = update.getMessage().getText();
        if (fromUser.equals("/start")){
            sendMessage.setText("Xush kelibsiz    " + update.getMessage().getChat().getFirstName()  +
                    " \n Bu bot so`mdan USD, EUR va CNY ga va teskari tartibda valyuta ayirboshlaydi " + " \n Valyutani tanlang");
            return sendMessage;
        }
        else if(fromUser.equals("UZS -> EUR") ||
                fromUser.equals("UZS -> USD") ||
                fromUser.equals("USD -> UZS") ||
                fromUser.equals("EUR -> UZS") ||
                fromUser.equals("UZS -> CNY") ||
                fromUser.equals("CNY -> UZS")){
            sendMessage.setText("Qiymatni kiriting:");
            return sendMessage;
        }
        return sendMessage.setText("Noma'lum buyruq");
    }

    public SendMessage uzsEur(Update update) {
        SendMessage sendMessage = new SendMessage();
        int amount = Integer.parseInt(update.getMessage().getText());
        double result = amount /eur;
        sendMessage.setText(result + " Euro");
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public SendMessage uzsUsd(Update update) {
        SendMessage sendMessage = new SendMessage();
        int amount = Integer.parseInt(update.getMessage().getText());
        double result = amount /usd;
        sendMessage.setText(result + " United States Dollars");
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }


    public SendMessage eurUzs(Update update)  {
        SendMessage sendMessage = new SendMessage();
        int amount = Integer.parseInt(update.getMessage().getText());
        double result = amount * eur;
        sendMessage.setText(result + " so`m");
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public SendMessage usdUzs(Update update){
        SendMessage sendMessage = new SendMessage();
        int amount = Integer.parseInt(update.getMessage().getText());
        double result = amount * usd;
        sendMessage.setText(result + " so`m");
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }
    public SendMessage cnyUzs(Update update)  {
        SendMessage sendMessage = new SendMessage();
        int amount = Integer.parseInt(update.getMessage().getText());
        double result = amount * cny;
        sendMessage.setText(result + " so`m");
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public SendMessage uzsCny(Update update){
        SendMessage sendMessage = new SendMessage();
        int amount = Integer.parseInt(update.getMessage().getText());
        double result = amount /cny;
        sendMessage.setText(result + " CNY");
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public ArrayList<Currency> currencies() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        Type type = new TypeToken<ArrayList<Currency>>(){}.getType();
        ArrayList<Currency> currencies = gson.fromJson(bufferedReader, type);
        return currencies;

    }

}


@Getter
@Setter
@ToString
class Currency {
    Integer id;
    Integer Code;
    String Ccy;
    String CcyNm_RU;
    String CcyNm_UZ;
    String CcyNm_UZC;
    String CcyNm_EN;
    Double Nominal;
    Double Rate;
    String Diff;
    String Date;

}