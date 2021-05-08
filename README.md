# 숙명여자대학교 IT공학전공 졸업프로젝트 관리 시스템: 티트리
#### Graduation project management system for IT Engineering Department, Sookmyung Women’s University

![졸업프로젝트관리시스템(티트리)_1714577 기규림 1713889 오정연_포스터 최종](https://user-images.githubusercontent.com/61455647/117541254-f687d700-b04d-11eb-8f4e-f35e07929323.png)

## 1. Introduction
[숙명여자대학교 IT공학전공 졸업프로젝트 관리 시스템 사이트](http://itserver1.sookmyung.ac.kr)

## 2. System Architecture Diagram
![image](https://user-images.githubusercontent.com/61455647/117542016-b4609480-b051-11eb-83d0-116f23c00172.png)


## 3. What are Used
![image](https://user-images.githubusercontent.com/61455647/117461544-a7c43980-af88-11eb-941c-09a0a4c4995b.png)

- Development
  - Java 1.8
  - Spring Boot 2.4.1
  - HTML+CSS+JavaScript+jQuery
- Database: MySQL 8.0
- Server: CentOS 8
- Build with Gradle 6.3
- Webserver: nginx
## 4. Database Tables
<table>
<tr>
<th>ROLE</th>
<th>TABLE NAME</th>
<th>TABLE EXPLANATION</th>
</tr>
<tr>
<td rowspan="2">졸업프로젝트 시행규칙/유의사항</td>
<td>project_notes</td>
<td>졸업프로젝트 유의사항 내용</td>
</tr>
<tr>
<td>project_rules</td>
<td>졸업프로젝트 시행규칙 내용</td>
</tr>
<tr>
<td rowspan="6">졸업작품 소개</td>
<td>board</td>
<td>졸업작품 소개 게시글</td>
</tr>
<tr>
<td>proposal_file</td>
<td>제안서(학생연구활동계획서) 파일</td>
</tr>
<tr>
<td>finalpt_file</td>
<td>최종발표 파일</td>
</tr>
<tr>
<td>fair_file</td>
<td>전시회 파일</td>
</tr>
<tr>
<td>source_file</td>
<td>소스코드 파일</td>
</tr>
<tr>
<td>paper_file</td>
<td>논문 파일</td>
</tr>
<tr>
<td rowspan="5">회원 관리</td>
<td>users</td>
<td>회원 정보</td>
</tr>
<tr>
<td>token</td>
<td>회원가입 시 사용되는 이메일 인증 토큰 정보</td>
</tr>
<tr>
<td>auth_image</td>
<td>회원가입 시 인증 파일(숙명포털 학적정보 캡처 화면)</td>
</tr>
<tr>
<td>team</td>
<td>졸업프로젝트 팀 정보</td>
</tr>
<tr>
<td>application_form</td>
<td>졸업프로젝트 신청서 파일</td>
</tr>
</table>

## 5. Documentation
### Manual

### Deployment
[AWS EC2를 이용하여 임시 배포하기(Deploy to AWS EC2)](https://github.com/kyurimki/T-Tree/issues/1)

[학교 서버에 배포하기(Deploy to Department's Server)](https://github.com/kyurimki/T-Tree/issues/2)

### Paper
[논문-Google Docs](https://docs.google.com/document/d/1mv3BFnLqHVSjI9tnkwRj2uay8IQE12bzN3EfWGCSBms/edit?usp=sharing)
