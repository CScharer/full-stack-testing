*** Settings ***
Documentation     Google Search Tests using Robot Framework
Library           SeleniumLibrary
Test Setup        Open Browser To Google
Test Teardown     Close Browser

*** Variables ***
${GOOGLE_URL}     https://www.google.com
${SEARCH_QUERY}   Robot Framework testing

*** Test Cases ***
Google Homepage Should Be Accessible
    [Documentation]    Verify that Google homepage loads correctly
    Title Should Be    Google
    Page Should Contain Element    name:q
    Page Should Contain Element    input[name="btnK"]

Perform Google Search
    [Documentation]    Test basic Google search functionality
    Input Text    name:q    ${SEARCH_QUERY}
    Press Keys    name:q    RETURN
    Wait Until Page Contains    ${SEARCH_QUERY}
    Page Should Contain Element    id:search

Search And Verify Results
    [Documentation]    Search for a term and verify results are displayed
    Input Text    name:q    Selenium WebDriver
    Press Keys    name:q    RETURN
    Wait Until Element Is Visible    id:search    timeout=10s
    Page Should Contain    Selenium
    ${result_count}=    Get Element Count    h3
    Should Be True    ${result_count} > 0    No search results found

*** Keywords ***
Open Browser To Google
    Open Browser    ${GOOGLE_URL}    chrome
    Maximize Browser Window
    Set Selenium Implicit Wait    5s

