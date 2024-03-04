# OA Toy Project — synthetic Override Assignment (OA) semantic conflicts

Toy Java project with a **hand-built git timeline** used to exercise semantic
conflict detection (specifically *Interprocedural Override Assignment* — `ioa`,
as described in *"The Effect of Call-Graph Construction Algorithms on Semantic
Conflict Detection"*).

Two developers branch from a common base and their changes merge **cleanly at
the text level** (no merge conflict), yet **semantically interfere**: both write
to the same state element (`Player.score`) and one write overrides the other
with no base value read in between — an **Override Assignment** conflict.

In every scenario **Alice = Left parent**, **Bob = Right parent**, and **Bob
(Right) overrides Alice (Left)** ("Right interferes with Left").

## Timeline

```
* Merge scenario 2  (bonus vs penalty — different methods, different classes)
|\
| * Bob:   reset score in PenaltyService.punish()
* | Alice: award bonus points in BonusService.award()
|/
* Merge scenario 1  (level-up bonus vs reset — same method)
|\
| * Bob:   reset score inside Player.levelUp()
* | Alice: add score bonus inside Player.levelUp()
|/
* Set up toy project (base)
```

`git log --merges` yields exactly two merge commits.

## Scenario 1 — same method

Both developers edit the **same method** `Player.levelUp()`:

- **Alice (Left):** `this.score = this.score + 20;`
- **Bob (Right):** `this.score = 0;`

The edits land on non-adjacent lines, so the 3-way merge succeeds. In the merged
method Bob's `score = 0` runs after Alice's `score += 20`, overriding it.

## Scenario 2 — different methods, different classes

The writes to the shared state element `Player.score` come from **different
methods in different classes**, both reachable from `Main.main`:

- **Alice (Left):** `BonusService.award(p)` → `p.setScore(p.getScore() + 50);`
- **Bob (Right):** `PenaltyService.punish(p)` → `p.setScore(0);`

`Main` calls `award(p)` then `punish(p)`, so Bob's overwrite overrides Alice's
award. This mirrors the paper's motivating example (`countDupWords` /
`countDupWhiteSpace` both writing to `fixes`), but split across two classes.

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
non-zero. Getting `0` shows Right fully overriding Left in both merges.
