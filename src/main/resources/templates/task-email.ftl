<html lang="en">
<body>
<p>Hello ${task.assignee.username}!</p>
<img src="cid:avatar_id" alt="avatar"/>
<p>Task <b>${task.name}</b> has been assigned to you. Due date: ${task.dueDate?string('yyyy-MM-dd')}</p>
<div>
    <#list task.subtasks>
        Subtasks:
        <ul>
            <#items as subtask>
                <li>${subtask.name}</li>
            </#items>
        </ul>
    </#list>
</div>
</body>
</html>