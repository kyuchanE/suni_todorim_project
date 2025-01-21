# Android Compose Architecture demo app

### 이 프로젝트는 Andriod Compose 기본 Architecture 설계를 목표로한 간단한 Todo 리스트 기능을 구현 했습니다.
### 지인분의 iOS 'ToDoRim - 할일관리, 미리알림'을 클론코딩 진행 했습니다.
[1인 앱 개발 : 기획부터 배포까지 ToDoRim (Suni Blog)](https://sunidev.tistory.com/29) </br>
[SuniDev - ToDoRim (github)](https://github.com/SuniDev/ToDoRim) </br>

## 👉 Project Architecture

![Image](https://github.com/user-attachments/assets/b45e4e13-72e8-44da-964b-44879c88b305)

- Android Developer 사이트에서 소개하는 'Guide to app architecture'를 적용 </br>
[Android Developer -Guide to app architecture](https://developer.android.com/topic/architecture) </br></br>
<img src = "https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview.png" width ="500" height = "300"/> </br>

## 👉 Multi Module + Navigator

#### 👍 멀티모듈의 이점
- 멀티 모듈 구조를 이용한다면 수정된 모듈만 재빌드가 진행되기 때문에 빌드 시간이 단축되는 이점을 얻을 수 있다.
- 코드 재사용성의 이점이 있다.

#### 📫 Navigator
- 멀티 모듈간의 소통과 호출을 위하여 생성
- Navigator Module에 Navigator를 각 Feature Module에서 상속 받아 멀티 모듈 사이의 호출에 사용했습니다. </br>

[다중 모듈 프로젝트](https://developer.android.com/guide/navigation/integrations/multi-module)
