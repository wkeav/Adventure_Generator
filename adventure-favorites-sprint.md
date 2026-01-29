# Adventure Favorites — Agile Sprint Plan

## 🎯 Feature Goal
Enable users to mark/unmark their own adventures as favorites, view favorites list, and persist state in DB.

---

## 📋 Sprint Slice 1 — Data Model & Repository
**Goal**: Add persistence layer for favorites

### Tasks
1. **Add `isFavorite` field to `Adventure` entity** DONE
   - Location: `src/main/java/Adventure_Generator/Model/Adventure.java`
   - Add field: `@Column(name="is_favorite") private Boolean isFavorite = false;`
   - Add getter/setter: `getIsFavorite()`, `setIsFavorite(Boolean isFavorite)`
   - Update constructor to include parameter (optional, default false)

2. **Update DB schema**
   - For dev: JPA `ddl-auto=update` will auto-create column on next boot
   - For prod: manual migration `ALTER TABLE adventure ADD COLUMN is_favorite boolean DEFAULT false;`

3. **Add repository method**
   - Location: `src/main/java/Adventure_Generator/Repository/AdventureRepository.java`
   - Add: `List<Adventure> findByUserIdAndIsFavoriteTrueOrderByCreatedAtDesc(Long userId);`

   

### Acceptance Criteria
- [ ] Application boots without errors
- [x] DB shows `is_favorite` column in `adventure` table
- [ ] Repository method compiles (no red squiggles)

### Hints
- Boolean fields should use `Boolean` (nullable) not `boolean` primitive
- JPA derives query from method name: `findBy[Field]And[Field][Condition]OrderBy[Field][Direction]`
- Default `false` ensures existing adventures aren't favorites

---

## �� Sprint Slice 2 — Service Logic
**Goal**: Business logic for toggling and fetching favorites

### Tasks
1. **Add `toggleFavorite` method to `AdventureService`**
   - Signature: `public Adventure toggleFavorite(Long adventureId, Long userId, boolean isFavorite)`
   - Steps:
     1. Fetch adventure by `adventureId` using `adventureRepository.findById()`
     2. Throw exception if not found or `adventure.getUser().getId() != userId`
     3. Set `adventure.setIsFavorite(isFavorite)`
     4. Save and return updated adventure
   - Make it idempotent: if already in desired state, just return without save

2. **Add `getFavoriteAdventures` method**
   - Signature: `public List<Adventure> getFavoriteAdventures(Long userId)`
   - Delegate to repository: `return adventureRepository.findByUserIdAndIsFavoriteTrueOrderByCreatedAtDesc(userId);`

3. **Create custom exception (optional)**
   - Create `NotFoundException` in `Adventure_Generator.Exception` package
   - Extends `RuntimeException`, used for 404 responses

### Acceptance Criteria
- [ ] `toggleFavorite` changes flag and saves
- [ ] `toggleFavorite` throws exception if wrong user
- [ ] `toggleFavorite` is idempotent (calling twice with same value works)
- [ ] `getFavoriteAdventures` returns only favorites for user

### Hints
```java
// Ownership check pattern
if (!adventure.getUser().getId().equals(userId)) {
    throw new NotFoundException("Adventure not found");
}

// Idempotency check
if (Boolean.TRUE.equals(adventure.getIsFavorite()) == isFavorite) {
    return adventure; // already in desired state
}
```

---

## 📋 Sprint Slice 3 — API Endpoints
**Goal**: Expose REST endpoints for favorites

### Tasks
1. **Add `POST /api/adventures/{id}/favorite` endpoint**
   - Location: `AdventureController.java`
   - Extract `id` from path with `@PathVariable Long id`
   - Get current user from `SecurityContextHolder.getContext().getAuthentication().getPrincipal()`
   - Call `adventureService.toggleFavorite(id, currentUser.getId(), true)`
   - Return `ResponseEntity.ok(updatedAdventure)`
   - Handle exceptions: catch `NotFoundException` → return 404

2. **Add `DELETE /api/adventures/{id}/favorite` endpoint**
   - Similar to POST but call `toggleFavorite(..., false)`
   - Return 200 on success

3. **Add `GET /api/adventures/favorites` endpoint**
   - Call `adventureService.getFavoriteAdventures(currentUser.getId())`
   - Return `ResponseEntity.ok(favoritesList)`

### Acceptance Criteria
- [ ] POST marks adventure as favorite (200 response)
- [ ] DELETE unmarks favorite (200 response)
- [ ] GET returns filtered list of favorites only
- [ ] All endpoints return 404 for non-existent/unauthorized adventures
- [ ] Endpoints require JWT authentication

### Hints
```java
@PostMapping("/{id}/favorite")
public ResponseEntity<?> markFavorite(@PathVariable Long id) {
    try {
        User currentUser = (User) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        Adventure updated = adventureService.toggleFavorite(id, currentUser.getId(), true);
        return ResponseEntity.ok(updated);
    } catch (NotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
```

---

## 📋 Sprint Slice 4 — DTO/Response Alignment
**Goal**: Ensure `isFavorite` appears in all responses

### Tasks
1. **Verify entity serialization includes `isFavorite`**
   - Test `GET /api/adventures/history` — should now show `isFavorite: false` for existing adventures
   - Test `POST /api/adventures/generate` — new adventures should show `isFavorite: false`

2. **Update `AdventureResponse` DTO (if used)**
   - Location: `src/main/java/Adventure_Generator/DTOs/Response/AdventureResponse.java`
   - Add field: `private Boolean isFavorite;`
   - Add to constructor and getter

### Acceptance Criteria
- [ ] All endpoints returning `Adventure` entities include `isFavorite` field in JSON
- [ ] Field defaults to `false` for new/existing adventures

### Hints
- Jackson auto-serializes entity fields to JSON
- If DTO is used, map `adventure.getIsFavorite()` to DTO field

---

## 📋 Sprint Slice 5 — Automated Tests
**Goal**: Ensure feature works and doesn't break existing flows

### Tasks
1. **Repository test (`AdventureRepositoryTest.java`)**
   - Test `findByUserIdAndIsFavoriteTrueOrderByCreatedAtDesc` filters correctly
   - Setup: save adventures with mixed `isFavorite` values for 2 users
   - Assert: query returns only user1's favorites, ordered newest first

2. **Service test (`AdventureServiceTest.java`)**
   - Test `toggleFavorite` sets flag and saves
   - Test ownership enforcement (throws exception for wrong user)
   - Test idempotency (calling twice doesn't fail)
   - Test `getFavoriteAdventures` returns filtered list

3. **Controller test (`AdventureControllerTest.java`)**
   - Test POST `/api/adventures/{id}/favorite` returns 200 with updated JSON
   - Test DELETE returns 200
   - Test POST with other user's adventure returns 404
   - Test GET `/favorites` returns correct filtered list
   - Use `@WithMockUser` or mock JWT principal

### Acceptance Criteria
- [ ] All new tests pass
- [ ] Existing tests still pass (no regressions)
- [ ] `./mvnw test` succeeds

### Hints
```java
// Service test setup
@Test
void toggleFavorite_marksAsFavorite() {
    User user = new User(1L, "test@test.com", "testuser", "pass", LocalDateTime.now());
    Adventure adv = new Adventure("hike", user, "happy", "sunny", false);
    adv.setId(1L);
    when(adventureRepository.findById(1L)).thenReturn(Optional.of(adv));
    when(adventureRepository.save(any())).thenReturn(adv);
    
    Adventure result = adventureService.toggleFavorite(1L, 1L, true);
    
    assertTrue(result.getIsFavorite());
    verify(adventureRepository).save(adv);
}
```

---

## 📋 Sprint Slice 6 (Optional) — Frontend Integration
**Goal**: Add UI for favoriting adventures

### Tasks
1. **Update adventure history UI**
   - Location: `src/main/resources/static/js/modules/adventureGenerator.js`
   - Add star/heart icon button next to each adventure
   - Show filled star if `isFavorite === true`, outline if false

2. **Wire toggle handler**
   - On click, call `POST /api/adventures/{id}/favorite` or `DELETE` based on current state
   - Update local state optimistically, rollback on error
   - Include JWT token in `Authorization: Bearer <token>` header

3. **Add Favorites view**
   - New button/tab to show only favorites
   - Fetch from `GET /api/adventures/favorites`
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
