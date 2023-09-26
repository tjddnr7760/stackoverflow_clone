<div align="center">
  <a href="https://github.com/tjddnr7760/stackoverflow_clone/assets/42529087/97f8f760-ae32-4d8a-8bd5-57cfd9765805">
    <img src="https://github.com/jonghyeon37/pre-practice/assets/124742974/4ad0d2cb-3af3-4e37-93f0-6e0a983028c9" alt="Logo" width="300" height="130">
  </a>
  <p align="center">
  스택오버플로우 클론
  </p>

  <p align="center">
    황성욱 담당 부분 소개 및 요약
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>목차</summary>
  <ol>
    <li><a href="#프로젝트-소개">프로젝트 소개</a></li>
    <li><a href="#아키텍처">아키텍처</a></li>
    <li><a href="#기술스택">기술 스택</a></li>
    <li><a href="#erd">ERD</a></li>
    <li><a href="#주요-기능-소개">주요 기능 소개</a></li>
  </ol>
</details>


## 프로젝트 소개
![image](https://github.com/jonghyeon37/pre-practice/assets/124742974/f18131ad-c7fd-4702-b099-e0cc9b541f49)  
![image](https://github.com/jonghyeon37/pre-practice/assets/124742974/4786bd7c-c974-4b0e-8d61-a99a372869f0)

저는 아래의 기능을 담당하여 하였습니다.

구현 목록:
* 로그인
* 회원가입
* 메인페이지
* 질문글 CRUD
* 답변글 CRUD
* 마이페이지 UPDATE, DELETE
* ERD 설계

## 아키텍처
![image](https://github.com/tjddnr7760/stackoverflow_clone/assets/42529087/75a7074d-5548-45c8-814d-cd42b5dc44c2)

## 기술스택
### 백엔드 기술 스택
Java 11,  Gradle,  Spring Boot,  Spring Data Jpa,  Spring Security,  Lombok,  MySql,  H2
### 인프라 기술 스택
Aws Ec2

## ERD
![image](https://github.com/tjddnr7760/stackoverflow_clone/assets/42529087/cdc8d5ac-9689-4f4b-8139-b2001e02b327)

## 주요 기능 소개
### 질문 목록 조회
질문 목록을 15개씩 최신순으로 불러옵니다.
### 질문 상세 조회
질문을 선택하면 상세 내용과 답변을 불러옵니다.
### 질문글, 답변글 CREATE, UPDATE, DELETE
질문과 답변을 생성하고 수정하고 삭제합니다.
### 로그인, 회원가입, 회원정보 UPDATE, DELETE
회원가입을 통해 로그인하고 회원정보를 수정하거나 삭제합니다.
