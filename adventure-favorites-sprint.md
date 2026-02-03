# Adventure Favorites — Agile Sprint Plan

## 🎯 Feature Goal
Enable users to mark/unmark their own adventures as favorites using a join-table model, view favorites list, and persist state in DB.

---

## 📋 Sprint Slice 1 — Persistence Layer (Join Table)
**Goal**: Model favorites as a relationship entity, not a boolean field.

### Tasks
1. **Create `UserFavorite` entity**
   - Location: `src/main/java/Adventure_Generator/Model/UserFavorite.java`
   - Fields: `Long id`, `User user`, `Adventure adventure`, `LocalDateTime createdAt`
   - Relationships:
     - `@ManyToOne(fetch = FetchType.LAZY)` for both `user` and `adventure`
   - Add `@PrePersist` to set `createdAt`

2. **Add DB constraints/indexes (via JPA or migration)**
   - Unique constraint on `(user_id, adventure_id)`
   - Index on `user_id`
   - Index on `adventure_id`

3. **Create `UserFavoriteRepository`**
   - Location: `src/main/java/Adventure_Generator/Repository/UserFavoriteRepository.java`
   - Methods:
     - `Optional<UserFavorite> findByUserAndAdventure(User user, Adventure adventure);`
     - `List<UserFavorite> findAllByUserId(Long userId);`
     - `void deleteByUserAndAdventure(User user, Adventure adventure);`

### Acceptance Criteria
- [ ] `UserFavorite` entity compiles
- [ ] Unique constraint prevents duplicate favorites
- [ ] Repository methods compile

### Hints
- Use `@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","adventure_id"}))`
- Use `@Index` for `user_id` and `adventure_id` for fast lookups

---

## 📋 Sprint Slice 2 — Service Logic (Favorites Service)
**Goal**: Add business logic for creating/removing favorites and retrieving favorited adventures.

### Tasks
1. **Create `FavoriteService`**
   - Location: `src/main/java/Adventure_Generator/Service/FavoriteService.java`
   - Methods:
     - `addFavorite(Long userId, Long adventureId)` (idempotent)
     - `removeFavorite(Long userId, Long adventureId)`
     - `isFavorited(Long userId, Long adventureId)`
   - Use `@Transactional` on write methods

2. **Resolve entities safely**
   - Load `User` and `Adventure` with repositories
   - Throw `NotFoundException` if either not found

3. **Idempotency**
   - Before creating, check `findByUserAndAdventure`
   - If already exists, return without duplicate write

### Acceptance Criteria
- [ ] `addFavorite` is idempotent
- [ ] `removeFavorite` deletes relationship
- [ ] `isFavorited` returns correct boolean

### Hints
```java
@Transactional
public void addFavorite(Long userId, Long adventureId) {
    // find user + adventure
    // if exists -> return
    // else save new UserFavorite
}
```

---

## 📋 Sprint Slice 3 — API Endpoints (Favorites)
**Goal**: Expose REST endpoints for favorites and define the API contract.

### Tasks
1. **Add `POST /api/favorites/{adventureId}` endpoint**
   - Location: `FavoriteController.java`
   - Extract `adventureId` with `@PathVariable`
   - Get current user from `SecurityContextHolder`
   - Call `favoriteService.addFavorite(currentUser.getId(), adventureId)`
   - Return 201 or 200

2. **Add `DELETE /api/favorites/{adventureId}` endpoint**
   - Call `favoriteService.removeFavorite(...)`
   - Return 200 or 204

3. **Add `GET /api/favorites` endpoint**
   - Return list of favorited adventures for current user
   - Uses DTO (see Slice 4)

4. **Swagger/OpenAPI annotations**
   - Add `@Operation` and `@ApiResponse` to endpoints

### Acceptance Criteria
- [ ] POST creates favorite
- [ ] DELETE removes favorite
- [ ] GET returns only current user's favorites
- [ ] Endpoints require JWT authentication

---

## 📋 Sprint Slice 4 — DTO/Response Alignment
**Goal**: Expose favorite status without polluting core entities.

### Tasks
1. **Add `favorited` flag to `AdventureResponse` DTO**
   - Location: `src/main/java/Adventure_Generator/DTOs/Response/AdventureResponse.java`
   - Field: `private boolean favorited;`

2. **Map DTO in service layer**
   - When returning adventures, set `favorited` by checking `UserFavorite`
   - Avoid adding user-specific fields to `Adventure` entity

### Acceptance Criteria
- [ ] API responses include `favorited` flag
- [ ] `Adventure` entity remains user-agnostic

---

## 📋 Sprint Slice 5 — Automated Tests
**Goal**: Ensure join-table favorites work and regressions are avoided

### Tasks
1. **Repository test (`UserFavoriteRepositoryTest.java`)**
   - Test `findByUserAndAdventure` and `findAllByUserId`
   - Setup: create favorites for multiple users

2. **Service test (`FavoriteServiceTest.java`)**
   - Test `addFavorite` idempotency
   - Test `removeFavorite` deletes
   - Test `isFavorited` works

3. **Controller test (`FavoriteControllerTest.java`)**
   - Test POST/DELETE/GET endpoints
   - Test 404 on missing adventure
   - Use `@WithMockUser` or mock JWT principal

### Acceptance Criteria
- [ ] All new tests pass
- [ ] Existing tests still pass (no regressions)
- [ ] `./mvnw test` succeeds

### Hints
```java
@Test
void addFavorite_isIdempotent() {
   // when exists, service should not create a duplicate
}
```

---

## 📋 Sprint Slice 6 (Optional) — Frontend Integration
**Goal**: Add UI for favoriting adventures with join-table API

### Tasks
1. **Update adventure history UI**
   - Location: `src/main/resources/static/js/modules/adventureGenerator.js`
   - Add star/heart icon button next to each adventure
   - Show filled star if `isFavorite === true`, outline if false

2. **Wire toggle handler**
   - On click, call `POST /api/favorites/{id}` or `DELETE` based on current state
   - Update local state optimistically, rollback on error
   - Include JWT token in `Authorization: Bearer <token>` header

3. **Add Favorites view**
   - New button/tab to show only favorites
   - Fetch from `GET /api/favorites`
   - Display same format as history

### Acceptance Criteria
- [ ] User can click star to toggle favorite
- [ ] UI updates immediately (optimistic)
- [ ] Favorites tab shows filtered list
- [ ] Works with JWT auth

### Hints
```javascript
async function toggleFavorite(adventureId, isFavorite) {
    const method = isFavorite ? 'DELETE' : 'POST';
    const token = localStorage.getItem('jwtToken');
    
    const response = await fetch(`/api/adventures/${adventureId}/favorite`, {
        method,
        headers: { 'Authorization': `Bearer ${token}` }
    });
    
    if (response.ok) {
        return await response.json();
    }
    throw new Error('Failed to toggle favorite');
}
```

---

## ✅ Definition of Done (Overall Feature)
- [ ] User can mark/unmark adventures as favorites via API
- [ ] Favorites persist in database
- [ ] GET /favorites returns only favorites for current user
- [ ] All endpoints enforce ownership (404 for other users' adventures)
- [ ] Test suite passes (unit + integration)
- [ ] No regressions in existing `/generate` and `/history` endpoints
- [ ] (Optional) Frontend shows favorite state and allows toggle

---

## 🔍 Testing Checklist (Manual QA)
1. Register/login to get JWT token
2. Generate a few adventures via `POST /generate`
3. Mark one as favorite: `POST /api/adventures/1/favorite` (use real ID)
4. Verify response shows `isFavorite: true`
5. Fetch history: `GET /api/adventures/history` — should show flag
6. Fetch favorites: `GET /api/adventures/favorites` — should show only favorite
7. Unmark: `DELETE /api/adventures/1/favorite`
8. Verify favorites list now empty

---

## 📚 Learning Goals (Tutor Mode)
- **Repository patterns**: Understand Spring Data JPA derived queries
- **Service layer**: Business logic separation, ownership validation
- **REST design**: Idempotent operations, proper HTTP verbs (POST/DELETE)
- **Testing pyramid**: Unit (service) → Integration (controller) → E2E (manual)
- **Agile delivery**: Ship in vertical slices (DB → service → API → UI)

---

## 🚀 Next Steps After Completion
- Add pagination to favorites/history endpoints
- Add adventure notes/tags
- Implement favorite count analytics
- Share favorites between users (new feature)
