# OA Toy Project — synthetic Override Assignment (OA) semantic conflicts

Toy Java project with a **hand-built git timeline** used to exercise semantic
conflict detection (specifically *Interprocedural Override Assignment* — `ioa`,
as described in *"The Effect of Call-Graph Construction Algorithms on Semantic
Conflict Detection"*).

Two developers branch from a common base and their changes merge **cleanly at
the text level** (no merge conflict), yet **semantically interfere**: both write
to the same state element (`Player.score`) and one write overrides the other
with no base value read in between — an **Override Assignment** conflict.

**Alice = Left parent**, **Bob = Right parent**, and **Bob (Right) overrides
Alice (Left)** ("Right interferes with Left").

## Timeline

```
*   Merge left and right
|\
| * Bob (right):  reset score in Player.levelUp() and PenaltyService.punish()
* | Alice (left): add points in Player.levelUp() and BonusService.award()
|/
* Set up toy project (base)
```

`git log --merges` yields exactly one merge commit.

## Changes (line numbers of the merge)

| Developer | Class | Line | Change |
|---|---|---|---|
| Alice (Left) | `Player.levelUp()` | 29 | `this.score = this.score + 20;` |
| Alice (Left) | `BonusService.award(p)` | 8 | `p.setScore(p.getScore() + 50);` |
| Bob (Right) | `Player.levelUp()` | 33 | `this.score = 0;` |
| Bob (Right) | `PenaltyService.punish(p)` | 8 | `p.setScore(0);` |

Two interferences on `Player.score`:

- **Same method:** in `Player.levelUp()`, Bob's `score = 0` runs after Alice's
  `score += 20`, overriding it. The edits land on non-adjacent lines, so the
  3-way merge succeeds.
- **Different methods, different classes:** `Main` calls `award(p)` then
  `punish(p)`, so Bob's reset overrides Alice's award. This mirrors the paper's
  motivating example (`countDupWords` / `countDupWhiteSpace` both writing to
  `fixes`), but split across two classes.

## Runtime evidence of the interference

```
$ javac -d out $(find src -name '*.java') && java -cp out org.example.Main
Level up to level 2
Current score is 20            <- Alice's +20 took effect...
BonusService.award called
PenaltyService.punish called
Final score: 0                 <- ...but Bob's resets override it (OA conflict)
```

If Alice's contributions were the ones preserved, the final score would be
non-zero. Getting `0` shows Right fully overriding Left.
