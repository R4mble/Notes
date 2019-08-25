class Channel {
   name: string
   receiveF: (c: Channel) => void
   sendF: () => void
   c: Channel

   constructor (name: string) {
      this.name = name
    }
  
    reduce (): void {
      if (!this.sendF || !this.receiveF) return
      console.log(`passing name ${this.c.name} via channel ${this.name}`)
      let rnd = Math.random()
      if (rnd >= 0.5) {
          this.sendF()
          this.receiveF(this.c)
      } else {
          this.receiveF(this.c)
          this.sendF()
      }
    }
  
    send (c: Channel, f: () => void): void {
      console.log(`${this.name} sending ${c.name}`)
      this.c = c
      this.sendF = f
      this.reduce()
    }
  
    receive (f: (c: Channel) => void): void {
      console.log(`${this.name} receiving`)
      this.receiveF = f
      this.reduce()
    }
  }
  
  const channel = (name: string) => new Channel(name)
  const x = channel('x')
  const y = channel('y')
  const w = channel('w')
  const a = channel('a')
  
  /**
   * 当函数可以是一等公民时,谁都不知道一个普普通通的盒子里究竟藏了什么猫腻.
   * 
   * 1. 说好的并发执行呢?
   * 答: js本身就是异步的,所以下面三个函数调用谁也不等谁,直接往后跑,算是并发执行了.
   * 
   * 2. receive不是接收一个Channel吗,怎么接收一个函数了?
   * 答: receive接收一个 接收Channel z,并使用z继续send或者receive 的函数.
   * 
   * 3. send为什么有两个参数?
   * 答: 第一个参数是它要send的Channel, 第二个参数是send之后的动作.
   */ 
  x.receive((z: Channel) => z.send(a, () => console.log('term 1 over')))
  x.send(w, () => y.send(w, () => console.log('term 2 over')))
  y.receive((v: Channel) => v.receive(u => (() =>
    console.log(`term 3 over, received ${u.name} finally`))()))