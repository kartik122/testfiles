import { Server, Socket } from "socket.io";
import http from "http";
import { clearTimeout } from 'timers'

let timerFunc = setTimeout(function(){
  console.log("time starts")
},20000);
let highestBid: number = 0;
let highestBidder: string = "";
let highestBidrSID: string = "";

class SetupSocket {
  private io: Server;
  private avatars: any = new Map();
  private clients: any = {}
  private currRoom: string = "";
  private rooms = [
    {
      name: "hall",
      desc: "This is default room",
    },
  ];
  private chairStatus: {[chairId: string]: string} = {ch1: '', ch2: '', ch3: '', ch4: '', ch5: '', ch6: '', ch7: '', ch8: '', ch9: '', ch10: '', ch11: '', ch12: '', ch13: '', ch14: '', ch15: '', ch16: '', ch17: '', ch18: ''}
  private bidStatus: boolean = false
  // private userNames: {}

  constructor(server: http.Server) {
    this.io = new Server(server, {
      cors: {
        origin: "http://localhost:3000",
        methods: ["GET", "POST"],
        credentials: true,
      },
      pingTimeout: 30000,
      pingInterval: 35000
    });

    this.io.on("connection", (socket: Socket) => {
      console.log(socket.id + " connected");
      this.clients[socket.id] = {}

      socket.on("disconnect", () => {
        console.log("socket disconnected : " + socket.id);
        socket.broadcast.to(this.currRoom).emit("leavingAvatar", socket.id);
        this.avatars.delete(socket.id);

        if (this.clients && this.clients[socket.id]) {
          console.log('deleting ' + socket.id)
          this.io.emit('removeClient', this.clients[socket.id].un)
          delete this.clients[socket.id]
        }

      });

      // socket.on("reconnect", )

      socket.on('newModel', (modelName: string, username: string) => {
        console.log('new model into scene: ' + username)
        if (this.clients[socket.id]) {
          this.clients[socket.id].mn = modelName
          this.clients[socket.id].un = username
          // this.clients[socket.id].pos = position
          // this.clients[socket.id].rot = rotation
          // this.clients[socket.id].aa = aa
          // this.clients[socket.id].sit = sit
        }
        this.io.emit('addNewModel', {avi: modelName, un: username})
      });

      socket.on('getCurrentModelsState', () => {
        console.log('req for current models')
        if(this.clients)
          socket.emit('currentModelsState', this.clients)
        else
          console.log('no users exist')
        
          // this.io.emit('updateClient', {
          //   mn: this.clients[socket.id].mn,
          //   un: this.clients[socket.id].un,
          //   pos: this.clients[socket.id].pos,
          //   rot: this.clients[socket.id].rot,
          //   sit: this.clients[socket.id].sit,
          //   aa: this.clients[socket.id].aa
          // })
      })

      // socket.on('getCurrentVideoState', () => {
      //   console.log('req for video state')
        
      // })

      // socket.emit('', '')


      socket.on('updateUser', (userInfo) => {
        if (this.clients[socket.id]) {
          console.log('update user: ' + this.clients[socket.id].un)
          // this.clients[socket.id].mn = userInfo.mn
          this.clients[socket.id].un = userInfo.un
          this.clients[socket.id].pos = userInfo.pos
          this.clients[socket.id].rot = userInfo.rot
          this.clients[socket.id].aa = userInfo.aa
          this.clients[socket.id].sit = userInfo.sit
          if(userInfo.sit)
            this.chairStatus[userInfo.sit] = this.clients[socket.id].un

          // this.io.emit()
          // this.io.emit('updateClients', this.clients)
          // this.io.emit('updateClient', {
          //   mn: this.clients[socket.id].mn,
          //   un: this.clients[socket.id].un,
          //   pos: this.clients[socket.id].pos,
          //   rot: this.clients[socket.id].rot,
          //   sit: this.clients[socket.id].sit,
          //   aa: this.clients[socket.id].aa
          // })
        }
      })

      socket.on('freedChair', (message: any) => {
        this.chairStatus[message] = ''
        // this.io.emit('freeTheChair', message)
        console.log('freed: ' + message)
      })

      socket.on('updateVidStatus', (vidInfo) => {
        console.log('vidPLay: ' + vidInfo.vidStatus)
        this.io.emit('updateVidPlayer', vidInfo)
      })

      // function emitBidStatus() {
      //   this.io.emit('updateBidStatus', this.bidStatus)
      // }

      socket.on('clientBid',(msg)=>{
        console.log("message.highestBid = ", msg.number)
        if(this.bidStatus) {
          if (msg.number > highestBid ){
            highestBid = msg.number;
            highestBidder = msg.metamaskAddress;
            clearTimeout(timerFunc);

            // this.io.emit('messageToClients', highestBid, socket.id)
            highestBidderSID = socket.id
            this.io.emit('highestBidUpdate', {un: this.clients[socket.id].un, bid: highestBid , metamaskAddress: msg.metamaskAddress })
            timerFunc = setTimeout(function(this: any) {
                //this.io.emit('timesup')
                console.log('times up')
                highestBid = 0;
                this.bidStatus = false
                // this.io.emit('updateBidStatus', this.bidStatus)
            }, 20000)
          }

          console.log(highestBid)
        }
        
      })

      socket.on('nftUpdate', (nftInfo) => {
        this.io.emit('nftUpdateBidders', nftInfo)
        this.io.emit('itemChange', nftInfo.screenStatus)
      })

      socket.on('biddingStatus', (bidStatus) => {
        this.bidStatus = bidStatus
        // if(!bidStatus)
        //   highestBid = 0
        this.io.emit('updateBidStatus', {bidStatus:this.bidStatus , highestBidder:highestBidder, highestBid:highestBid});
        if(bidStatus === false) {
          socket.broadcast.to(highestBidderSID).emit('payNotification', highestBid)
        }
      })

      socket.on('updateAuctionItem', (index) => {
        this.io.emit('itemChange', index)
        highestBid = 0
        if(this.bidStatus) {
          this.bidStatus = false
          this.io.emit('updateBidStatus', this.bidStatus)
        }
      })
      socket.on('updateDynamicAuctionItem', (name) => {
        this.io.emit('dynamicItemChange', name)
        highestBid = 0
        if(this.bidStatus) {
          this.bidStatus = false
          this.io.emit('updateBidStatus', this.bidStatus)
        }
      })

      socket.on('dynamic_item', (itemInfo) => {
        console.log('dynamic item')
        this.io.emit('updateDynamicItem', itemInfo);
      })

      socket.on('changedScale', (scale) => {
        this.io.emit('updateScale', scale)
      })

      socket.on('changedYPos', (yPos) => {
        this.io.emit('updateYPos', yPos)
      })

      socket.on('changedXPos', (yPos) => {
        this.io.emit('updateXPos', yPos)
      })

      socket.on('changedZPos', (yPos) => {
        this.io.emit('updateZPos', yPos)
      })

      socket.on('messageToServer',(dataFromClient)=>{
        console.log(dataFromClient)
      })


      socket.on('newChatToServer',(msgInfo)=>{
        console.log(msgInfo)
        if(!(this.bidStatus && msgInfo.mn != 'host'))
          this.io.emit('messageToClients', msgInfo)
      })

    });

    setInterval(() => {
      this.io.emit('updateClients', this.clients)
      this.io.emit('chairStatus', this.chairStatus)
    }, 50)

  }
}

export default SetupSocket;
