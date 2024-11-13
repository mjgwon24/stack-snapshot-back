
# 🧑‍💻 Stack Snapshot (Backend) - 사진 추억 남기기 서비스

[![Contributors](https://img.shields.io/badge/contributors-4-brightgreen)](#-기여자-contributors)  

**Stack Snapshot**은 '인생네컷'과 유사한 기능을 제공하는 사진 추억 남기기 서비스로, **경북 2024 해커톤**에서 참가자들의 소중한 추억을 기록해주는 프로젝트입니다. 사용자는 다양한 프레임을 적용해 사진을 찍고, 소중한 추억을 남길 수 있습니다. 이 프로젝트의 백엔드는 **Spring Boot**와 **JPA**를 기반으로 이루어져있습니다.

---

## 🧑‍💻 기여자

| 이름         | Github 프로필            | 역할                              | 사용 언어 |
|--------------|--------------------------|-----------------------------------|-------------|
| **권민지** | [mjgwon24](https://github.com/mjgwon24) | PM & frontend & backend           | Java |
| **김이현** | [lh7721004](https://github.com/lh7721004) | frontend & backend        | Java |
| **임석진** | [seokjin925](https://github.com/seokjin925) | frontend & backend                 | Java |
| **이수헌** | [suheon927](https://github.com/suheon927) | backend                 | Java |

---

## 🛠️ 기술 스택

- **언어:** Java
- **프레임워크:** Spring Boot
- **빌드 도구:** Gradle
- **기타:** REST API (클라이언트와의 데이터 통신)

---

## 🔧 주요 기능

- **사진 찍기:** 사용자는 다양한 필터를 적용하여 사진을 찍을 수 있습니다.
- **추억 저장:** 최종적으로 생성된 사진을 다운로드할 수 있습니다.
- **QR 코드 제공:** 사진이 생성된 후, 다운로드를 위한 QR 코드가 제공됩니다.

---

## 📂 프로젝트 구조

```plaintext
src/
├── main/java/stackup/stack_snapshot_back/
│   ├── config/              # 프로젝트 설정 관련 파일
│   ├── controller/          # API 엔드포인트 컨트롤러
│   ├── restController/      # RESTful API 엔드포인트 정의
│   ├── service/             # 비즈니스 로직 처리
│   ├── util/                # 유틸리티 클래스
│   └── StackSnapshotBackApplication.java  # 애플리케이션 진입점
└── test/java/stackup/stack_snapshot_back/  # 테스트 코드
```

---

## 💻 설치 방법

1. 저장소 클론

```bash
git clone https://github.com/mjgwon24/stack-snapshot-back.git
```

2. 프로젝트 디렉터리로 이동

```bash
cd stack-snapshot-back
```

3. 필요한 의존성 설치

```bash
./gradlew build
```

4. 로컬 서버 실행

```bash
./gradlew bootRun
```

5. 로컬에서 `http://localhost:8080` 에 접속하여 서비스 확인

---

## 📬 문의

프로젝트 관련 문의는 GitHub Issues 또는 아래 연락처로 보내주세요.

- **GitHub Repository:** [stack-snapshot-back](https://github.com/mjgwon24/stack-snapshot-back.git)

- **이메일:** alswlchlrh8@naver.com
