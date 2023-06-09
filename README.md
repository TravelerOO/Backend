# 🍀 다녀왔쥬?
# 백엔드 파트 

## &#127774; 팀원소개

<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/seonminKim1122"><img src="https://avatars.githubusercontent.com/u/124031561?v=4" width="100px;" alt=""/><br /><sub><b>BE 리더 : 김선민</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/bbakzi"><img src="https://avatars.githubusercontent.com/u/128371819?v=4" width="100px;" alt=""/><br /><sub><b>BE 팀원 : 박지훈</b></sub></a><br /></td>
        <td align="center"><a href="https://github.com/kkj5158"><img src="https://avatars.githubusercontent.com/u/40461588?v=4" width="100px;" alt=""/><br /><sub><b>BE 팀원 : 김지승</b></sub></a><br /></td>
        <td align="center"><a href="https://github.com/YEJINGO"><img src="https://avatars.githubusercontent.com/u/114003526?v=4" width="100px;" alt=""/><br /><sub><b>BE 팀원 : 고예진</b></sub></a><br /></td>
    </tr>
  </tbody>
</table>

## 📊 프로젝트 설명
계절을 기준으로 나만의 여행지를 추천(비추천)하는 사이트 

## ⏳ 개발 기간
2023년 5월 5일 ~ 2023년 5월 11일 


## ⚙️프로젝트 구조 

![Slide 16_9 - 기술스택기술스택3](https://github.com/TravelerOO/Backend/assets/40461588/2ad6e60d-b018-46fb-9fe1-5eb1bf26011c)


### 전체적인 구조/흐름

1. 유저에게 제공되는 프론트엔드단 서버를 통해서 백엔드 서버에 요청이 전달됩니다. 
2. 전체적인 백엔드 서버는 AWS EC2에 구축된 상황입니다. 해당 서버에는 언제나 완성되어서 안정화된 버전들만 올라갑니다. 
3. 이 안정화된 버전들은 깃허브 액션이 깃허브에서 메인 브랜치에 안정화된 버전이 풀리퀘스트를 통해서 들어올때 자동 배포해줍니다.
4. 그 전까지는 . 팀원들이 협업하여 만들어진 결과물들은 develop 브랜치에 차곡차곡 커밋되어 쌓아집니다. 특정 버전 배포가 준비되었다고 판단시에. 
main 브렌치에 pr을 날립니다. ( 버전관리 시스템은 git/github를 사용합니다.) 
5. 백엔드 서버는 스프링부트와 이를 기반으로하는 하위 프레임워크 기술들로 구성되어있습니다 ,
6. 데이터베이스서버에 sql 쿼리를 날리고 이를 관리하는 ORM기술은 SPRING DATA JPA를 사용하고 있습니다. 
7. 데이터베이스 서버는 AWS RDS(mysql) / AWS S3(이미지 파일) 입니다.  
8. 협업툴은 이미지와 같이 노션,피그마.기타 툴들을 사용합니다.  

### 기술 키워드들 

#### 스프링 부트와 하위 기술들로 구성된 백엔드 서버

스프링부트를 기반으로 저희가 사용하고 있는 하위기술들은 다음과 같습니다. 핵심기술들 위주로 요악하면 

- 스프링 시큐리티 : 인증/인가 . 보안 처리 
- 스프링 mvc : 컨트롤러 , 레포지토리, 서비스단의 구조로 나뉘어진 mvc 패턴2 적용을 위한 하위 프레임워크
- 스프링 DATA JPA : ORM 기술인 JPA을 템플릿화한 프레임워크로 데이터베이스단 접근 기술 

이외에도 스프링의 하위기술은 아니여도 다양한 기술들을 적용하고 있습니다 .

- JWT : 인증/인가를 관리하는 기술
- OAUTH : 소셜로그인 관련 인증/인가 관리하는 기술 
- 스웨거 : 프로젝트 구조를 파악하여 api를 자동생성해주는 기술 
- REdis : 인메모리 기술로, 로그인, 로그아웃을 관리해주는 기술  

#### 데이터베이스 접근 기술 _ ORM < Spring Data Jpa> 

우리 프로젝트에서는 객체를 객체답게 사용하자는 철학에 따라 개발된 JPA를 조금 템플릿화 시킨 SPRING DATA JPA를 사용합니다,
필요시에는 JPQL를 혼용하여서 사용합니다. 

#### VCS ( 버전 컨트롤 시스템 ) _ GIT/GITHUB 

대표적인 버전 관리 시스템인 GIT/GITHUB를 사용하여서 프로젝트의 전체 버전을 관리하고 있습니다. 

#### CD 와 GITHUB ACTIONS 

우리 프로젝트에서는 메인 브런치에 새로운 코드들이 들어왔을때, 이 시점을 트리거 삼아서 깃허브 액션이 코드를 자동 배포해줍니다.
