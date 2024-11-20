package AmericanEagleTest;

import java.security.SecureRandom;
import java.util.List;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class Utils {

    //скролим к элементу, чтобы он был в центре экрана
    public static void scrollToElement(SelenideElement element){executeJavaScript("arguments[0].scrollIntoView({block: 'center'});", element);}

    //кликаем на случайный элемент из заданного множества аналогичных внутри введённого элемента-контейнера
    public static void clickRandomElement(SelenideElement container, SelenideElement option){
        System.out.println("Выбирает один из доступных элементов для клика.");
        long startTime = System.currentTimeMillis();
        List<SelenideElement> options = container.$$(option.getSearchCriteria()).stream().toList();
        System.out.println("Всего текстовых элементов обнаружено: " + options.size());
        if(!options.isEmpty()){
            int index = new SecureRandom().nextInt(options.size());
            System.out.println("Выбираем: " + options.get(index).shouldBe(visible).getText());
            options.get(index).click();
        } else System.out.println("Доступных элементов нет.");
        System.out.println("Затрачено времени: " + (System.currentTimeMillis() - startTime) + " мс.");
    }

    //навести мышь на случайный элемент из заданного множества аналогичных внутри введённого элемента-контейнера
    public static SelenideElement hoverRandomElement(SelenideElement container, SelenideElement option){
        System.out.println("Выбирает один из доступных элементов для наведения мыши.");
        long startTime = System.currentTimeMillis();
        List<SelenideElement> options = container.$$(option.getSearchCriteria()).stream().toList();
        System.out.println("Всего текстовых элементов обнаружено: " + options.size());
        if(!options.isEmpty()){
            int index = new SecureRandom().nextInt(options.size());
            System.out.println("Выбираем: " + options.get(index).shouldBe(visible).getText());
            scrollToElement(options.get(index));
            actions().moveToElement(options.get(index)).perform();
            System.out.println("Затрачено времени: " + (System.currentTimeMillis() - startTime) + " мс.");
            return options.get(index);
        }
        System.out.println("Доступных элементов нет.");
        System.out.println("Затрачено времени: " + (System.currentTimeMillis() - startTime) + " мс.");
        return null;
    }

}