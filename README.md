# AssignmentAP
Assignment advanced programing, CSE - HCMUT

Lưu ý về workflow:
Khi được giao task(trên trello hoặc zalo):
1. New 1 branch from dev với tên là feature được giao và làm việc trên branch đó
2. Sau khi hoàn thành task và chạy các unitest thì tiến hành tạo pull request để leader review code trước khi merge và branch dev
3. Sau khi leader review và approve pull request thì merge feature và branch dev
   + Trường hợp branch dev có commit ahead thì người merge feature có nhiệm vụ giải quyết các confict(nếu có) sau đó tiến hành rebase & merge
   + Trường hợp branch up-to-date với branch dev thì tiến hành merge bình thường
