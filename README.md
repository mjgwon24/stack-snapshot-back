
# 🧑‍💻 Stack Snapshot (Backend) - 사진 추억 남기기 서비스

<img width="1725" alt="스크린샷 2024-12-03 18 06 21" src="https://github.com/user-attachments/assets/c65b1d28-420b-4349-8791-9afb76e213e5">

**Stack Snapshot**은 '인생네컷'과 유사한 기능을 제공하는 사진 추억 남기기 서비스로, **경북 2024 해커톤**에서 참가자들의 소중한 추억을 기록해주는 프로젝트입니다. 사용자는 다양한 프레임을 적용해 사진을 찍고, 소중한 추억을 남길 수 있습니다.

<br/>
<br/>

## 🧑‍💻 기여자
[![Contributors](https://img.shields.io/badge/contributors-5-brightgreen)](#-기여자-contributors)  

| 이름         | Github 프로필            | 역할                              | 사용 언어 |
|--------------|--------------------------|-----------------------------------|-------------|
| **권민지** | [mjgwon24](https://github.com/mjgwon24) | PM & frontend & backend           | Java, JavaScript, React |
| **김이현** | [lh7721004](https://github.com/lh7721004) | frontend & backend        | Java, JavaScript, React |
| **임석진** | [seokjin925](https://github.com/seokjin925) | frontend & backend                 | Java, JavaScript, React |
| **이수헌** | [suheon927](https://github.com/suheon927) | backend                 | Java |
| **김현나** | [nanaring1](https://github.com/nanaring1) | frontend                | JavaScript, React |
| **구세영** | [wybh930](https://github.com/wybh930) | frontend               | JavaScript, React |

<br/>
<br/>

## 🛠️ 개발 환경

### 협업 툴
   ![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white)  ![Discord](https://img.shields.io/badge/Discord-5865F2?style=flat&logo=discord&logoColor=white)   ![Notion](https://img.shields.io/badge/Notion-000000?style=flat&logo=notion&logoColor=white)

### 서비스 배포 환경
  ![Ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white) ![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)

<br/>
<br/>

## 🔧 기술 스택

### Language  
![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white)

### Backend  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white)  ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)  ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black)  ![REST API](https://img.shields.io/badge/REST%20API-005571?style=flat&logo=rest&logoColor=white)

### Cooperation  
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white)  ![GitHub Projects](https://img.shields.io/badge/Projects-0366D6?style=flat&logo=github&logoColor=white)

<br/>
<br/>

## 🔄 Development Workflow

### 브랜치 전략

브랜치 전략은 Git Flow를 기반으로 하며, 다음과 같은 브랜치를 사용합니다.

- **Main Branch**:  
    - 배포 가능한 상태의 코드를 유지합니다.  
    - 모든 배포는 이 브랜치에서 이루어집니다.
- **Develop Branch**:
    - 기능 개발을 취합하는 브랜치입니다.
- **Feature Branches**:  
    - 기능별로 분기한 브랜치입니다.
    - 각 기능 개발은 `feature/{기능명}` 형식의 브랜치에서 이루어지며, 작업 완료 후 Develop Branch로 병합됩니다.

<br/>
<br/>

## 🔧 주요 기능

- **사진 찍기:** 사용자는 다양한 필터를 적용하여 사진을 찍을 수 있습니다.
- **추억 저장:** 최종적으로 생성된 사진을 다운로드할 수 있습니다.
- **QR 코드 제공:** 사진이 생성된 후, 다운로드를 위한 QR 코드가 제공됩니다.

<br/>
<br/>

## 📅 개발 기간

- **개발 기간**: 2024년 10월 13일 ~ 2024년 11월 20일  

<br/>
<br/>

## 📑 페이지별 기능

### [ 프레임 선택 ]
다양한 프레임 중에서 원하는 디자인을 선택하여 사진에 적용할 수 있습니다.

<img width="950" alt="스크린샷 2024-12-03 18 43 20" src="https://github.com/user-attachments/assets/bfe759ba-07e4-47cd-9212-f4bcdc91dfc0">

<br/>

### [ 사진 촬영 ]

사용자는 카메라를 통해 실시간으로 사진을 촬영할 수 있습니다.

<img width="950" alt="스크린샷 2024-12-03 18 43 20" src="https://github.com/user-attachments/assets/623f892d-0965-4bb4-a3eb-4ed9b99676e2">

<br/>

### [ 사진 선택 ]

사용자가 원하는 사진을 선택하고 편집할 수 있습니다.

<img width="950" alt="스크린샷 2024-12-03 19 23 21" src="https://github.com/user-attachments/assets/f48ed16e-5369-4fe8-8db6-8349e64ccd8f">

<br/>
### [ QR 코드 다운로드 ]

QR 코드를 생성하여 사진 다운로드 링크를 제공합니다.

<img width="950" alt="스크린샷 2024-12-03 19 25 06" src="https://github.com/user-attachments/assets/5ee50394-9f66-4300-93eb-a6bd961c0b49">
<img width="950" alt="스크린샷 2024-12-03 19 26 33" src="https://github.com/user-attachments/assets/d130d986-ee14-4946-abb2-14c93e87f8ba">

<br/>
### [ 경품 뽑기 ]

이벤트 경품 뽑기 페이지를 통해 경품을 뽑을 수 있습니다.

<img width="950" alt="스크린샷 2024-12-03 19 28 14" src="https://github.com/user-attachments/assets/b6ae2e7f-ed93-41eb-aab0-6463a1c2934b">
<img width="950" alt="스크린샷 2024-12-03 19 28 39" src="https://github.com/user-attachments/assets/5b25d942-d2db-4f52-9751-526b3767a1ae">


<br/>
<br/>

## 📬 프로젝트 후기

### 🍒 권민지

프로젝트 초기에는 아이디어 구체화부터 설계까지 많은 고민이 필요했습니다. 특히 "사진 촬영과 프레임 선택을 통해 특별한 추억을 만들어주자"는 아이디어를 현실화하기 위해 구체적인 설계와 명확한 협업 규칙을 세우는 것이 중요했습니다.<br/>
이러한 과정은 **협업에서의 커뮤니케이션**과 **계획의 중요성**을 절실히 깨닫는 계기가 되었습니다.<br/>

또한 이번 프로젝트는 기본적인 프로그래밍 언어와 기술 스택들을 다시 한 번 다지고 성장할 수 있는 기회가 되었습니다. 프론트엔드와 백엔드, 그리고 사용자 경험(UX/UI)에 대한 고민을 하며 전체적인 시스템의 흐름을 깊이 이해할 수 있었습니다.<br/>
특히, 예상치 못한 버그와 기술적 난관을 해결하는 과정은 저 자신에게도 큰 도전이었고, 팀의 역량을 확인할 수 있는 값진 시간이었습니다.<br/>
무엇보다도 이번 프로젝트를 성공적으로 마무리할 수 있었던 가장 큰 이유는, 각자 맡은 역할을 책임감 있게 수행해준 팀원들이 있었기에 가능한 일이었습니다.<br/>
특히, 저를 믿고 따라와준 팀원들에게 진심으로 고맙고, 정말 수고했다는 말을 전하고 싶습니다.<br/>

해커톤 현장에서 예상보다 많은 참가자들이 스택네컷 서비스를 이용해 사진을 찍어주었고, "재미있다", "아이디어가 독특하다"는 긍정적인 반응을 많이 들을 수 있었습니다. 우리가 기획했던 작은 서비스가 실제로 사람들에게 웃음과 추억을 남겨줄 수 있었다는 점에서 큰 보람을 느꼈습니다. 이번 프로젝트를 통해 기술적 성장뿐만 아니라 **팀워크의 중요성**과 **사용자 중심의 사고**를 다시금 배울 수 있었습니다.<br/>
이러한 경험을 바탕으로 앞으로도 더 나은 프로젝트를 기획하고, 팀원들과 함께 성장하며, 새로운 도전에 나서고 싶습니다.<br/>

스택네컷 프로젝트를 함께해준 모든 팀원들과 응원해주신 분들께 감사드립니다♥️

_스택네컷 팀장_  

<br/>

### 👩‍💻 구세영

이번에 스택네컷 활동을 하면서 정말 다양한 경험을 가졌습니다!<br/>
아직 1학년이고 컴퓨터 공부를 시작한지 얼마 안 돼서 많이 어려웠지만, 그 과정에서 새로운 것도 많이 알게되었고, 많은 것들을 얻었습니다.<br/>
프론트엔드, 백엔드라는 용어 조차 몰랐던 저는 이번 활동을 통해 많이 알게되었습니다.<br/>
혼자 하면서 어려웠던 것들은 선배님들이 많이 알려주셨고, 많은 것들을 배웠습니다.<br/>
매주 회의에서 제가 몰랐던 용어들, 사이트, 구현 과정 등 알게됐습니다. 다들 너무 감사드리고, 수고많았습니다 !!

<br/>

### 🍯 김이현

스택네컷 프로젝트를 진행하면서 다양한 기능개발도 하고 환경구축도 하면서 너무 재밌었습니다.<br/>
팀원분들도 프로젝트에 적극적으로 참여해주시고, 무엇보다 민지PM님께서 프로젝트를 진행하는 방식에 대해서 많이 배울 수 있었고, 체계적으로 프로젝트를 잘 이끌고 나가주신 덕분에 올해 제가 한 프로젝트중 가장 재미있는 프로젝트가 될 수 있었던 것 같습니다!<br/>
이 프로젝트를 진행하면서 Springboot의 Service, Repository, Controller에 대해서 잘 배울 수 있었고, 브랜치 전략를 사용해서 프로젝트를 해본적이 있지만, 다양한 방면에서 아직 미숙한 부분이 있다고 느꼈고, 앞으로 제가 보완해나가야 할 방향을 잡을 수 있게 된 프로젝트였습니다.<br/>
다들 정말 수고하셨습니다!

<br/>

### 🧑‍💻 이수헌

이번에 백엔드 개발을 경험할 수 있는 기회가 주어져 정말 좋았습니다.<br/>
웹 프로그래밍의 전반적인 흐름을 이해하게 되어 개인적으로 매우 유익한 시간이었던 것 같습니다.<br/>
다양한 툴과 기술 스택을 접하면서 배움의 즐거움을 느꼈고, 실제로 적용해 볼 수 있었던 점도 큰 도움이 되었습니다.<br/>
능력이 부족해서 모든 부분을 개발할 수 없었던 아쉬움도 있지만, 이번 경험을 바탕으로 앞으로 더 다양한 것들을 시도하고 싶다는 의지가 생겼습니다.<br/>
앞으로는 부족했던 점을 보완하고, 보다 체계적으로 기록하며 성장해 나갈 수 있기를 기대합니다.<br/>
팀원들과 협업을 통해 많은 것을 배웠고, 서로의 노력이 모여 프로젝트를 완성해낸 것이 정말 뿌듯했습니다.

<br/>

### 🥥 임석진

벡엔드 개발을 처음 경험해볼 수 있었던 소중한 기회였고, 능력 있는 팀장과 팀원들과의 소통의 중요성을 깨달은 좋은 시간이었습니다. <br/>
겉으로는 간단해 보였던 프로젝트였지만, 실제로 부족했던 저의 역량을 알게 해주었고 조금씩 공부하며 성장할 수 있었습니다. <br/>
팀원들 또한 다른 프로젝트와 학업, 시험, 대회 등 바쁜 와중에도 다들 각자의 자리에서 최선을 다하여 성공적으로 마무리 될 수 있었던 것 같습니다.  <br/>
아쉬웠던 점이 있다면 짧은 시간내에 기능 개발위주로 구현을 하게되어  페이지별 혹은 컨트롤러, 서비스 등의 정합성 검증, junit등 테스트코드 작성같은 화이트박스 테스트를 진행해보고 싶었지만 주어진 시간자원이 부족했던 점 인것 같습니다. <br/>
다음 프로젝트에선 공정을 최소화하여 기능구현 뿐 아니라 검증도 철저히 해보고 싶습니다.💛<br/>

<br/>
