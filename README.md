![header](https://capsule-render.vercel.app/api?type=waving&color=gradient&height=300&section=header&text=Android-MutsaMarket&fontSize=50&fontAlignY=40&desc=2023-2%20고급모바일프로그래밍%20&descAlign=80)
<br>

## 프로젝트 개요
> 2023 고급모바일프로그래밍 - 멋사마켓

파이어베이스 백엔드 서비스를 활용하는 중고거래 안드로이드 앱을 개발하는 프로젝트를 수행합니다. <br>
프로젝트를 통해 안드로이드에서 파이어베이스 연결/사용 방법을 익히고 다양한 파이어베이스 서비스를 활용할 수 있습니다.

<br><br>

## 🦁 Contributor
<table>
  <tr> 
    <td><a href="https://github.com/gesal03"><img src="https://github.com/MutsaMarket/MutsaMarket-apk/assets/77336664/c4c89e9b-d53f-4f77-9817-ce1139d405a7" style="width:100%;"></a></td>
    <td><a href="https://github.com/beomtae"><img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/89a73b69-e889-4e04-842d-aca97c554371" style="width:100%;"></a></td>
    <td><a href="https://github.com/suzinlim"><img src="https://github.com/MutsaMarket/MutsaMarket-apk/assets/77336664/b533419f-6439-466d-a2f3-9eccd4c5bdc5" style="width:100%;"></a></td>
    <td><a href="https://github.com/sengooooo"><img src="https://github.com/MutsaMarket/MutsaMarket-apk/assets/77336664/67df1c19-c0e0-490a-afa8-b8cf97e9bc40" style="width:100%;"></a></td>
    
  </tr>
  <tr> 
    <td align='center'><strong>이현승</strong></td> 
    <td align='center'><strong>박태범</strong></td> 
    <td align='center'><strong>임수진</strong></td> 
    <td align='center'><strong>이세은</strong></td> 
  </tr>
</table>

<br><br>

## 1. 회원가입 & 로그인
### 1) 회원가입
<p float="left">
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/65b861b7-b67a-4be1-bc4d-8ed97ce8582b" width="300" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/a32a7c4d-db71-4eb8-89da-7492fcf97fac" width="300" />
</p>

* 이름, 생년월일, 이메일, 비밀번호를 입력하여 회원가입을 합니다.
* 이메일을 입력할 때는 <strong>이메일 형식</strong>에 맞추어 입력해주어야 하고, <strong>이메일 중복 확인 검사</strong>가 필요합니다.
* 비밀번호는 <strong>6~15자리 사이의 영문</strong>으로 입력해야 합니다.

### 2) 로그인
<p float="left">
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/50ef8dc3-0dcc-4b4f-bde1-607aa1a42c9c" width="300" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/a78f8506-429e-4cfb-a469-26b3c5aa67f0" width="300" />
</p>

* 회원가입 시 입력한 이메일과 비밀번호로 로그인할 수 있습니다.
* 정보가 일치하지 않으면 "이메일과 비밀번호를 확인해주세요."라는 Toast 메시지가 나타납니다.

### 3) 마이페이지
<p float="left">
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/f461d3ee-41c0-489f-afb7-82b1e2987563" width="300" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/65ed11f9-7a30-4654-a341-9b92ec1509bd" width="300" />
</p>

* 사용자의 정보는 마이페이지에서 확인할 수 있습니다.
* `로그아웃` 버튼을 누르면 로그아웃 처리가 됩니다.
* 회원 탈퇴를 하고 싶다면 `회원 탈퇴` 버튼을 눌러 "회원 탈퇴"를 입력하면 사용자 탈퇴 처리가 됩니다.

<br><br>

## 2. 판매 글 등록 / 수정

<p float="left">
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/1b153898-d832-4d99-9e31-40943f9f5768" width="300" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/17c16e35-1c66-42ae-b268-812a70b884fd" width="300" />
</p>

* 사용자는 판매할 상품의 이미지, 상품 이름, 판매 가격, 상품 설명을 입력한 후, `등록하기` 버튼을 눌러 등록할 수 있습니다.
* <strong>상품 이미지를 반드시 첨부</strong>하여야 글을 등록할 수 있습니다.
* 처음 판매 글을 등록하게 되면 판매 상태가 활성화되어 있으며, 사용자는 `수정하기`를 통해 판매 상태를 비활성화시킬 수 있습니다.

<p float="left">
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/8d322448-7af3-4c13-89a4-f7bbdde4334f" width="300" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/366a21df-6201-4008-996e-64699ddb1643" width="300" />
</p>

* 사용자가 작성한 글은 오른쪽 상단에 `수정하기` 버튼이, 사용자가 작성한 글이 아니면 `채팅하기` 버튼이 나타납니다.
* `수정하기` 버튼을 누르면 상품을 등록하는 화면이 나타나 원하는 내용을 수정할 수 있습니다.
* `채팅하기` 버튼을 누르면 해당 상품을 판매하고 있는 사용자에게 메시지를 보낼 수 있는 채팅창이 나타납니다.
  
<br><br>

## 3. 판매 글 목록

<p float="left">
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/a0ade5c8-fe8f-4f05-a072-145cf138378a" width="250" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/73924e67-e10e-4f2b-8916-ae3b8d275b78" width="250" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/f9bc245b-b571-41bc-8df1-9aef9e274a82" width="250" />
</p>

* `전체`, `판매 중`, `판매 완료`로 나누어 판매 글 목록을 확인할 수 있습니다.
* `전체`에서는 판매 중이거나 판매 완료된 모든 상품의 목록이 보여집니다.
* `판매 중`에서는 판매 중인 물품들의 목록이 보여집니다.
* `판매 완료`에서는 판매가 완료된 물품들의 목록이 보여집니다.

<br><br>

## 4. 판매자와 구매자 간의 채팅

### 메세지 보내기
<p float="left">
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/366a21df-6201-4008-996e-64699ddb1643" width="300" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/4ecff8b9-5086-4107-ac30-6566e76f6cbf" width="300" />
</p>

* 게시글 상세보기 페이지의 `채팅하기` 버튼을 통해 사용자와의 채팅을 시작할 수 있습니다.
* `채팅하기` 버튼 클릭시 다음과 같은 채팅화면으로 전환되며 전송버튼을 통해 채팅을 보낼 수 있습니다.

### 메세지 받기
<p float="left">
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/599dd3b3-b8f9-42b1-90c1-70178f64c761" width="300" />
  <img src="https://github.com/MutsaMarket/Android-MutsaMarket/assets/122861956/3caa9e7f-2d24-4373-bf60-8f5c05f6d4c4" width="300" />
</p>

* 사용자는 나에게 온 채팅들을 확인할 수 있습니다.
* 상대에게 받은 메세지는 왼쪽, 내 메세지는 오른쪽에 보이게 되며 상대방과 채팅을 주고 받을 수 있습니다.

<br><br>

## 주요 기술
<p>
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/android studio-3DDC84?style=for-the-badge&logo=android studio&logoColor=white">
  <img src="https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white">
</p>

<br>
