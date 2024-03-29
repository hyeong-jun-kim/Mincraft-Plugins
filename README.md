# 마인크래프트 플러그인 외주
### 1. GuestBook (방명록 플러그인)
**외주기간:** 2023.06.01 ~ 2023.06.02 <br>
Version: 1.19 (Spigot)
<details>
<summary>
구현 내용
</summary>
    
### OP권한
    
**/방명록 목록** - 생성된 항목들 나열 ✅

**/방명록 보기 [항목명] [페이지]** - (대소문자 구별 x) 해당 항목에 남겨진 글 리스트를 확인 (페이지란 미 입력 시, 1페이지 처리) ✅

**/방명록 글삭제 [항목명] [번호]** - 해당 항목에서 선택한 번호의 글을 제거함. ✅

**/방명록 생성 [항목명]** - 새 항목을 생성. *✅*

**/방명록 항목삭제 [항목명]** - 해당 항목의 데이터를 제거함. ✅

### 유저권한

**/방명록 작성 [항목명] [할말]** - 해당 항목에 방명록을 작성 ✅

**/방명록 삭제 [항목명]** - 자신이 작성한 해당 항목의 글을 제거 ✅

### 기타

- prefix : §f§l[ §a§l방명록 §f§l] §f ⇒ /방명록 칠 때 나오는 명령어 리스트에 추가하기 ✅
- 저장방식, 항목 별 다른 데이터 파일에 글 저장 ✅
- 1페이지당 6개씩 ✅
- 글 양식: [ 글 번호 ] [ 글 작성자 ] [ 내용 ] [ 남긴일자(yyyy.MM.dd) ] ✅
- 한 계정당 항목별 하나의 글만 작성할 수 있음. (하나의 글 이상 쓰면 덮어쓰기.) ✅
</details>

### 2. Vault-Check (수표 플러그인)
**외주기간:** 2023.06.24 ~ 2023.06.27 <br>
**Dependency:** Vault, ST-Money-1.0 [ 돈 ], nbt-api <br>
Version: 1.16.5 (Arclight)
<details>
<summary>
구현 내용
</summary>

### 수표 시스템

수표마다 고유의 코드가 있어, 동일 코드가 2개 이상 생길 시 해당 수표를 가지고 있는 사람을 밴 처리함 [복사버그 방지 시스템]

### 커맨드

- /수표 발행 [금액]
    - 수표 발행시 남는 돈에서 차감됨
    - 해당하는 금액만큼 돈이 있는지 체크하는게 필요함
- /수표 최대금액 [금액] - 수표화 가능한 최대 금액을 설정할 수 있습니다. - OP
- /수표 최소금액 [금액] - 수표화 가능한 최소 금액 설정할 수 있습니다. - OP

### 수표 상세내용

- **DisplayName:** &a[ &f수표 &a] &71000
- **Lore:** 우클릭 시 돈이 지급됩니다

### 밴되는 방식

- 이미 사용한 수표를 다시 사용할 경우
- A 유저가 발행한 수표를 B 유저가 사용하는 경우
    - 이 경우 수표를 발행한 유저가 밴당함

### 파일 양식

**""파일 양식[CopyBugLog.yml]""**

```java
Rib_ble:
버그 의심자: 'Rib_ble:: (버그 의심으로 밴처리 되었습니다)'
수표 발행 금액: 30000
발행/처리 시간: '2021-12-07
```
