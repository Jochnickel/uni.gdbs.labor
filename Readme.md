1. The Shell cycles until exited

2. Cycle: show cursor, read user input, execute user input, back to start.

3. Execution: Executes every BLOCK that is separated by semicolon.

4. BLOCK: Contains CHUNKS. Ones CHUNK success/fail leads to the next CHUNK. Elvis Grammar.

5. CHUNK: multiple commands that are piped. Fails/stops on any fail.

6. Command: program with params, stdin, stdout and stderr. Pipes initially set stdin/out. stdin/out/err maybe overwritten with redirection.


How a BLOCK gets executed:
delimit  ||
delimit &&
delimit |
scan <a and >a