[when]There is someone=$p: Person()
[when]- at least {age} years old=age>={age}
[when]- works as {jobTitle}=jobTitle=="{jobTitle}"
[then]hire him or her=$p.hire();