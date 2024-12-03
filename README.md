
# 🧑‍💻 Stack Snapshot (Backend) - 사진 추억 남기기 서비스

<img width="1725" alt="스크린샷 2024-12-03 18 06 21" src="https://github.com/user-attachments/assets/c65b1d28-420b-4349-8791-9afb76e213e5">

**Stack Snapshot**은 '인생네컷'과 유사한 기능을 제공하는 사진 추억 남기기 서비스로, **경북 2024 해커톤**에서 참가자들의 소중한 추억을 기록해주는 프로젝트입니다. 사용자는 다양한 프레임을 적용해 사진을 찍고, 소중한 추억을 남길 수 있습니다.

---

## 🧑‍💻 기여자
[![Contributors](https://img.shields.io/badge/contributors-4-brightgreen)](#-기여자-contributors)  

| 이름         | Github 프로필            | 역할                              | 사용 언어 |
|--------------|--------------------------|-----------------------------------|-------------|
| **권민지** | [mjgwon24](https://github.com/mjgwon24) | PM & frontend & backend           | Java |
| **김이현** | [lh7721004](https://github.com/lh7721004) | frontend & backend        | Java |
| **임석진** | [seokjin925](https://github.com/seokjin925) | frontend & backend                 | Java |
| **이수헌** | [suheon927](https://github.com/suheon927) | backend                 | Java |

---

## 🛠️ 개발 환경

### 협업 툴
   ![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white)  ![Discord](https://img.shields.io/badge/Discord-5865F2?style=flat&logo=discord&logoColor=white)   ![Notion](https://img.shields.io/badge/Notion-000000?style=flat&logo=notion&logoColor=white)

### 서비스 배포 환경
  ![Ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white) ![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)

---

## 🔧 기술 스택

### Language  
![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white)

### Backend  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white)  ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)  ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black)  ![REST API](https://img.shields.io/badge/REST%20API-005571?style=flat&logo=rest&logoColor=white)

### Cooperation  
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white)  ![GitHub Projects](https://img.shields.io/badge/Projects-0366D6?style=flat&logo=github&logoColor=white)

---

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

---

## 🔧 주요 기능

- **사진 찍기:** 사용자는 다양한 필터를 적용하여 사진을 찍을 수 있습니다.
- **추억 저장:** 최종적으로 생성된 사진을 다운로드할 수 있습니다.
- **QR 코드 제공:** 사진이 생성된 후, 다운로드를 위한 QR 코드가 제공됩니다.

---

## 📅 개발 기간

- **개발 기간**: 2024년 10월 13일 ~ 2024년 11월 20일  

---

## 📑 페이지별 기능

### [ 프레임 선택 ]
다양한 프레임 중에서 원하는 디자인을 선택하여 사진에 적용할 수 있습니다.

<img width="950" alt="스크린샷 2024-12-03 18 43 20" src="https://github.com/user-attachments/assets/bfe759ba-07e4-47cd-9212-f4bcdc91dfc0">


### [ 사진 촬영 ]

사용자는 카메라를 통해 실시간으로 사진을 촬영할 수 있습니다.

<img width="950" alt="스크린샷 2024-12-03 18 43 20" src="https://github.com/user-attachments/assets/623f892d-0965-4bb4-a3eb-4ed9b99676e2">



### [ 사진 선택 ]

사용자가 원하는 사진을 선택하고 편집할 수 있습니다.

<img width="950" alt="스크린샷 2024-12-03 19 23 21" src="https://github.com/user-attachments/assets/f48ed16e-5369-4fe8-8db6-8349e64ccd8f">


### [ QR 코드 다운로드 ]

QR 코드를 생성하여 사진 다운로드 링크를 제공합니다.

<img width="950" alt="스크린샷 2024-12-03 19 25 06" src="https://github.com/user-attachments/assets/5ee50394-9f66-4300-93eb-a6bd961c0b49">
<img width="950" alt="스크린샷 2024-12-03 19 26 33" src="https://github.com/user-attachments/assets/d130d986-ee14-4946-abb2-14c93e87f8ba">


### [ 경품 뽑기 ]

이벤트 경품 뽑기 페이지를 통해 경품을 뽑을 수 있습니다.

<img width="950" alt="스크린샷 2024-12-03 19 28 14" src="https://github.com/user-attachments/assets/b6ae2e7f-ed93-41eb-aab0-6463a1c2934b">
<img width="950" alt="스크린샷 2024-12-03 19 28 39" src="https://github.com/user-attachments/assets/5b25d942-d2db-4f52-9751-526b3767a1ae">


---

## 📬 문의

프로젝트 관련 문의는 GitHub Issues 또는 아래 연락처로 보내주세요.

- **GitHub Repository:** [stack-snapshot-back](https://github.com/mjgwon24/stack-snapshot-back.git)

- **이메일:** alswlchlrh8@naver.com
