from selenium import webdriver

import time

from selenium.webdriver.support.ui import WebDriverWait

from selenium.webdriver.common.action_chains import ActionChains

driver = webdriver.Chrome("/opt/chromedriver/chromedriver")

driver.get("http://localhost:8080/Turnierverwaltung/index.xhtml")

newTournamentXPath = "//a[contains(@href, 'newTournament')]"
newTournamentElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_xpath(newTournamentXPath))
newTournamentElement.click()
time.sleep(1)

tournamentNameId = "j_idt55:j_idt58"
tournamentNameElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(tournamentNameId))
tournamentNameElement.clear()
tournamentNameElement.send_keys("Schulturnier")
time.sleep(1)
tournamentSystemXPath = "html/body/div[4]/div/form/div/div/ul/li[5]"
tournamentSystemElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_xpath(tournamentSystemXPath))
actionChains = ActionChains(driver)
actionChains.double_click(tournamentSystemElement).perform()
time.sleep(1)
saveButtonId = "j_idt55:j_idt59"
saveButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(saveButtonId))
saveButtonElement.click()
time.sleep(1)
nextButtonId = "menu_steps:j_idt31"
nextButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(nextButtonId))
nextButtonElement.click()
time.sleep(1)

anzahlTeamsId = "teams_select:teamSliderText"
anzahlTeamsElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(anzahlTeamsId))
anzahlTeamsElement.clear()
anzahlTeamsElement.send_keys("16")
time.sleep(1)
groupSizeId = "teams_select:groupSizeSliderText"
groupSizeElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(groupSizeId))
groupSizeElement.clear()
groupSizeElement.send_keys("4")
time.sleep(1)
pointsWonId = "teams_select:pointsWonSliderText"
pointsWonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(pointsWonId))
pointsWonElement.clear()
pointsWonElement.send_keys("5")
time.sleep(1)
pointsDrawId = "teams_select:pointsDrawSliderText"
pointsDrawElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(pointsDrawId))
pointsDrawElement.clear()
pointsDrawElement.send_keys("3")
time.sleep(1)
nextButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(nextButtonId))
nextButtonElement.click()
time.sleep(1)

teamNameXPath = "html/body/div[4]/div/div/div/table/tbody/tr[1]/td[2]/form/input[2]"
teamNameElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_xpath(teamNameXPath))
teamNameElement.clear()
teamNameElement.send_keys("Bayern Muenchen")
time.sleep(1)
saveTeamNameId = "dataTable:0:j_idt70:j_idt72"
saveTeamNameElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(saveTeamNameId))
saveTeamNameElement.click()
time.sleep(1)
nextButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(nextButtonId))
nextButtonElement.click()
time.sleep(1)

startButtonId = "j_idt53:startTournamentBtn"
startButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(startButtonId))
startButtonElement.click()
time.sleep(1)

nextRoundButtonId = "j_idt17:btnNextRound"
nextRoundButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(nextRoundButtonId))
nextRoundButtonElement.click()
time.sleep(1)
nextRoundButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(nextRoundButtonId))
nextRoundButtonElement.click()
time.sleep(1)
nextRoundButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(nextRoundButtonId))
nextRoundButtonElement.click()
time.sleep(1)
nextRoundButtonElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(nextRoundButtonId))
nextRoundButtonElement.click()
time.sleep(1)

homeId = "html/body/form/div/ul/li[1]"
homeElement = WebDriverWait(driver, 10).until(lambda driver: driver.find_element_by_id(homeId))
homeElement.click()