package at.htl.turnierverwaltung.business.team;

import static org.junit.Assert.*;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adrian on 26.06.16.
 */
public class SeleniumTests {
    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "/opt/chromedriver/chromedriver");
        driver = new ChromeDriver();
        baseUrl = "localhost:8080/Turnierverwaltung/";
    }

    @Test
    public void test01noTournamentName() throws Exception {
        driver.get(baseUrl);
        WebElement element;
        driver.findElement(By.xpath("//a[contains(@href, 'newTournament')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(".//*[@id='j_idt53:startTournamentBtn']")).click();
        Thread.sleep(1000);
        for (int i = 0; i < 4; i++) {
            driver.findElement(By.xpath(".//*[@id='j_idt16:btnNextRound']")).click();
            Thread.sleep(1000);
        }
        Thread.sleep(15000);
        driver.findElement(By.xpath(".//*[@id='j_idt6:j_idt9']/ul/li[1]/a")).click();
        Thread.sleep(1000);
        element = driver.findElement(By.xpath(".//*[@id='j_idt16:tournament_table_data']/tr[2]/td[2]"));
        Assert.assertFalse(element.getText().equals(""));
    }

    @Test
    public void test02teamsMoreThanOnceInTournamentTree() throws Exception {
        driver.get(baseUrl);
        WebElement element1;
        WebElement element2;
        driver.findElement(By.xpath("//a[contains(@href, 'newTournament')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(".//*[@id='j_idt53:startTournamentBtn']")).click();
        Thread.sleep(1000);
        for (int i = 0; i < 4; i++) {
            driver.findElement(By.xpath(".//*[@id='j_idt16:btnNextRound']")).click();
            Thread.sleep(1000);
        }
        Thread.sleep(15000);
        element1 = driver.findElement(By.xpath(".//*[@id='j_idt17']/tbody/tr[1]/td[5]"));
        element2 = driver.findElement(By.xpath(".//*[@id='j_idt17']/tbody/tr[7]/td[5]"));
        Assert.assertTrue(element1.getText().equals(element2.getText()));
    }

    @Test
    public void test03changeNameOfATeam() throws Exception {
        driver.get(baseUrl);
        WebElement element;
        driver.findElement(By.xpath("//a[contains(@href, 'newTournament')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("dataTable:0:j_idt70:Name")).clear();
        driver.findElement(By.id("dataTable:0:j_idt70:Name")).sendKeys("FC Bayern");
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        element = driver.findElement(By.xpath(".//*[@id='j_idt73:j_idt94_data']/tr[1]/td"));
        Assert.assertFalse(element.getText().equals("FC Bayern"));
    }

    @Test
    public void test04noTournamentSystemChosen() throws Exception {
        driver.get(baseUrl);
        WebElement element;
        driver.findElement(By.xpath("//a[contains(@href, 'newTournament')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        element = driver.findElement(By.xpath(".//*[@id='j_idt73:j_idt75_data']/tr/td"));
        Assert.assertFalse(element.getText().equals(" "));
    }
    @Test
    public void test05FullTournament() throws Exception {
        driver.get(baseUrl);
        Random random = new Random();
        WebElement element;
        String path = "";
        Actions action = new Actions(driver);
        driver.findElement(By.xpath("//a[contains(@href, 'newTournament')]")).click();
        Thread.sleep(1000);
        element = driver.findElement(By.xpath("html/body/div[4]/div/form/div/div/ul/li[5]"));
        action.doubleClick(element).perform();
        Thread.sleep(1000);
        driver.findElement(By.xpath(".//*[@id='j_idt55:j_idt58']")).clear();
        driver.findElement(By.xpath(".//*[@id='j_idt55:j_idt58']")).sendKeys("Schulturnier");
        Thread.sleep(1000);
        driver.findElement(By.id("j_idt55:j_idt59")).click();
        Thread.sleep(1000);
        /*element = driver.findElement(By.xpath("//div[@id='teams_select:j_idt41']/span"));
        action.clickAndHold(element).moveByOffset(20, 0).release(element).build().perform();
        Thread.sleep(1000);*/
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(10000);
        /*driver.findElement(By.xpath(".//*[@id='teams_select:j_idt41']/span")).click();
        for (int i = 0; i < 11; i++) {
            action.keyDown(Keys.ARROW_RIGHT).perform();
            action.keyUp(Keys.ARROW_RIGHT).perform();
        }*/
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("menu_steps:j_idt31")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(".//*[@id='j_idt53:startTournamentBtn']")).click();
        Thread.sleep(1000);

        Thread.sleep(1000);
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:"+j+":ResultT"+i+"']")).clear();
                driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:"+j+":ResultT"+i+"']"))
                        .sendKeys(String.valueOf(random.nextInt(6)));
                Thread.sleep(1000);
            }
        }

        driver.findElement(By.xpath(".//*[@id='j_idt16:btnNextRound']")).click();
        Thread.sleep(1000);
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:"+j+":ResultT"+i+"']")).clear();
                driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:"+j+":ResultT"+i+"']"))
                        .sendKeys(String.valueOf(random.nextInt(6)));
                Thread.sleep(1000);
            }
        }
        driver.findElement(By.xpath(".//*[@id='j_idt16:btnNextRound']")).click();
        Thread.sleep(1000);
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:"+j+":ResultT"+i+"']")).clear();
                driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:"+j+":ResultT"+i+"']"))
                        .sendKeys(String.valueOf(random.nextInt(6)));
                Thread.sleep(1000);
            }
        }

        driver.findElement(By.xpath(".//*[@id='j_idt16:btnNextRound']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:0:ResultT1']")).clear();
        driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:0:ResultT1']")).sendKeys(String.valueOf(random.nextInt(6)));
        Thread.sleep(1000);
        driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:0:ResultT2']")).clear();
        driver.findElement(By.xpath(".//*[@id='j_idt16:dataTable:0:ResultT2']")).sendKeys(String.valueOf(random.nextInt(6)));
        Thread.sleep(1000);
        driver.findElement(By.xpath(".//*[@id='j_idt16:btnNextRound']")).click();
        Thread.sleep(15000);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
