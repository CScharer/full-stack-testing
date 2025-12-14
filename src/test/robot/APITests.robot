*** Settings ***
Documentation     API Tests using Robot Framework
Library           RequestsLibrary
Library           Collections

*** Variables ***
${BASE_URL}       https://jsonplaceholder.typicode.com

*** Test Cases ***
Get Posts API Test
    [Documentation]    Test GET request to retrieve posts
    Create Session    jsonplaceholder    ${BASE_URL}
    ${response}=    GET On Session    jsonplaceholder    /posts/1
    Should Be Equal As Strings    ${response.status_code}    200
    ${body}=    Set Variable    ${response.json()}
    Dictionary Should Contain Key    ${body}    id
    Dictionary Should Contain Key    ${body}    title
    Dictionary Should Contain Key    ${body}    body

Get All Posts Test
    [Documentation]    Test GET request to retrieve all posts
    Create Session    jsonplaceholder    ${BASE_URL}
    ${response}=    GET On Session    jsonplaceholder    /posts
    Should Be Equal As Strings    ${response.status_code}    200
    ${posts}=    Set Variable    ${response.json()}
    Should Not Be Empty    ${posts}
    Length Should Be    ${posts}    100

Create Post Test
    [Documentation]    Test POST request to create a new post
    Create Session    jsonplaceholder    ${BASE_URL}
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${data}=    Create Dictionary    title=Test Post    body=Test Body    userId=1
    ${response}=    POST On Session    jsonplaceholder    /posts    json=${data}    headers=${headers}
    Should Be Equal As Strings    ${response.status_code}    201
    ${body}=    Set Variable    ${response.json()}
    Dictionary Should Contain Key    ${body}    id
    Should Be Equal    ${body}[title]    Test Post

*** Keywords ***
Create Session
    [Arguments]    ${alias}    ${url}
    ${session}=    Create Session    ${alias}    ${url}
    [Return]    ${session}

